package com.flacko.payment.verification.receipt.impl;

import com.flacko.common.currency.Currency;
import com.flacko.common.exception.PaymentNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.service.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification.MAX_RECEIPT_SIZE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptPaymentVerificationServiceImpl implements ReceiptPaymentVerificationService {

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;
    private final ServiceLocator serviceLocator;
    private final RestTemplate restTemplate;
    @Value("${receipt.data.extractor.url}")
    private String receiptDataExtractorUrl;

    @Override
    public ReceiptPaymentVerificationListBuilder list() {
        return serviceLocator.create(ReceiptPaymentVerificationListBuilder.class);
    }

    @Override
    public ReceiptPaymentVerification get(String id) throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRepository.findById(id)
                .orElseThrow(() -> new ReceiptPaymentVerificationNotFoundException(id));
    }

    @Transactional
    @Override
    public ReceiptPaymentVerification verify(MultipartFile file, String paymentId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException {
        if (file.isEmpty()) {
            throw new ReceiptPaymentVerificationRequestValidationException("Please upload a file.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new ReceiptPaymentVerificationRequestValidationException("Uploaded file is not a PDF.");
        }

        if (file.getSize() > MAX_RECEIPT_SIZE) {
            throw new ReceiptPaymentVerificationRequestValidationException("File size exceeds 256 KB.");
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path tempDir = Files.createTempDirectory("uploads");
            Path filePath = tempDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String fileAbsolutePath = filePath.toAbsolutePath().toString();

            List<String> patterns = readPatterns();

            System.out.println(patterns);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Prepare form data
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("file", new FileSystemResource(fileAbsolutePath));
            for (String pattern : patterns) {
                formData.add("patterns", pattern);
            }

            // Prepare request entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

            System.out.println("Request Body: " + requestEntity.getBody());

            // Make HTTP POST request
            ResponseEntity<ReceiptExtractedData> response = restTemplate.postForEntity(
                    receiptDataExtractorUrl,
                    requestEntity,
                    ReceiptExtractedData.class);


            if (response.getStatusCode() == HttpStatus.OK) {
                return createReceiptPaymentVerification(Objects.requireNonNull(response.getBody()), file, paymentId);
            } else {
                log.warn(String.format("Payment %s verification failed. verificationResponse=%s", paymentId, response));
                throw new ReceiptPaymentVerificationFailedException(paymentId);
            }
        } catch (Exception e) {
            throw new ReceiptPaymentVerificationFailedException(paymentId, e);
        }
    }

    private ReceiptPaymentVerification createReceiptPaymentVerification(
            ReceiptExtractedData extractedData, MultipartFile file, String paymentId)
            throws IOException, ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, ReceiptPaymentVerificationInvalidAmountException,
            PaymentNotFoundException, ReceiptPaymentVerificationUnexpectedAmountException,
            ReceiptPaymentVerificationInvalidCardLastFourDigitsException {
        ReceiptPaymentVerificationBuilder builder = serviceLocator.create(ReceiptPaymentVerificationBuilderImpl.class)
                .initializeNew();
        builder.withPaymentId(paymentId)
                .withRecipientFullName(extractedData.getRecipientFullName())
                .withRecipientCardLastFourDigits(extractedData.getRecipientCardLastFourDigits())
                .withSenderFullName(extractedData.getSenderFullName())
                .withSenderCardLastFourDigits(extractedData.getSenderCardLastFourDigits())
                .withAmount(parseBigDecimal(extractedData.getAmount()))
                .withAmountCurrency(parseCurrency(extractedData.getAmountCurrency()))
                .withCommission(parseBigDecimal(extractedData.getCommission()))
                .withCommissionCurrency(parseCurrency(extractedData.getCommissionCurrency()))
                .withData(extractedData.getData())
                .withUploadedFile(file.getBytes());
        return builder.build();
    }

    private BigDecimal parseBigDecimal(String amount) throws ReceiptPaymentVerificationInvalidAmountException {
        String cleanedAmountString = amount.replace(" ", "").replace(",", ".");

        try {
            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            decimalFormat.setDecimalFormatSymbols(symbols);
            return new BigDecimal(decimalFormat.parse(cleanedAmountString).toString());
        } catch (ParseException e) {
            throw new ReceiptPaymentVerificationInvalidAmountException(amount);
        }
    }

    private Currency parseCurrency(String currency) throws ReceiptPaymentVerificationCurrencyNotSupportedException {
        if ("R".equals(currency)) {
            return Currency.RUB;
        }
        throw new ReceiptPaymentVerificationCurrencyNotSupportedException(currency);
    }

    private List<String> readPatterns() throws IOException {
        List<String> patterns = new ArrayList<>();
        try (InputStream inputStream = new ClassPathResource("pattern/receipt/patterns.csv").getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    patterns.add(parts[2]);
                }
            }
        }
        return patterns;
    }

}
