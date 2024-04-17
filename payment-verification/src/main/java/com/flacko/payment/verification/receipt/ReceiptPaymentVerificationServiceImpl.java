package com.flacko.payment.verification.receipt;

import com.flacko.payment.verification.bank.pattern.BankPattern;
import com.flacko.payment.verification.bank.pattern.BankPatternRepository;
import com.flacko.payment.verification.bank.pattern.BankPatternType;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.payment.verification.receipt.exception.ReceiptPaymentVerificationValidationException;
import com.flacko.payment.verification.receipt.rest.ReceiptPaymentVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.flacko.payment.verification.receipt.ReceiptPaymentVerification.MAX_RECEIPT_SIZE;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptPaymentVerificationServiceImpl implements ReceiptPaymentVerificationService {

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;
    private final ApplicationContext context;
    private final RestTemplate restTemplate;
    private final BankPatternRepository bankPatternRepository;

    @Override
    public List<ReceiptPaymentVerification> list() {
        return StreamSupport.stream(receiptPaymentVerificationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptPaymentVerification get(String id) throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRepository.findById(id)
                .orElseThrow(() -> new ReceiptPaymentVerificationNotFoundException(id));
    }

    @Override
    public ReceiptPaymentVerification verify(ReceiptPaymentVerificationRequest receiptPaymentVerificationRequest) throws ReceiptPaymentVerificationValidationException {
        MultipartFile file = receiptPaymentVerificationRequest.file();

        if (file.isEmpty()) {
            throw new ReceiptPaymentVerificationValidationException("Please upload a file.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new ReceiptPaymentVerificationValidationException("Uploaded file is not a PDF.");
        }

        if (file.getSize() > MAX_RECEIPT_SIZE) {
            throw new ReceiptPaymentVerificationValidationException("File size exceeds 256 KB.");
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path tempDir = Files.createTempDirectory("uploads");
            Path filePath = tempDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String fileAbsolutePath = filePath.toAbsolutePath().toString();

            List<String> patterns = bankPatternRepository.findByType(BankPatternType.RECEIPT)
                    .stream()
                    .map(BankPattern::getPattern)
                    .toList();

            ResponseEntity<ReceiptExtractedData> response = restTemplate.postForEntity(
                    "http://localhost:5000/payment-verification/receipt/extract-data",
                    new ReceiptExtractDataRequest(file, patterns),
                    ReceiptExtractedData.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ReceiptPaymentVerificationPojo receiptPaymentVerification = createReceiptPaymentVerification(Objects.requireNonNull(response.getBody()), receiptPaymentVerificationRequest);
                receiptPaymentVerificationRepository.save(receiptPaymentVerification);
                return receiptPaymentVerification;
            } else {
                throw new ReceiptPaymentVerificationValidationException("Failed to extract data from receipt. "
                        + response);
            }
        } catch (Exception e) {
            throw new ReceiptPaymentVerificationValidationException("An error occurred during receipt verification.",
                    e);
        }
    }

    private ReceiptPaymentVerificationPojo createReceiptPaymentVerification(ReceiptExtractedData extractedData, ReceiptPaymentVerificationRequest receiptPaymentVerificationRequest) throws IOException, ReceiptPaymentVerificationMissingRequiredAttributeException {
        ReceiptPaymentVerificationBuilder builder = context.getBean(ReceiptPaymentVerificationBuilderImpl.class)
                .initializeNew();
        builder.withPaymentId(receiptPaymentVerificationRequest.paymentId())
                .withRecipientFullName(extractedData.recipientFullName())
                .withRecipientCardLastFourDigits(extractedData.recipientCardLastFourDigits())
                .withSenderFullName(extractedData.senderFullName())
                .withSenderCardLastFourDigits(extractedData.senderCardLastFourDigits())
                .withAmount(extractedData.amount())
                .withAmountCurrency(extractedData.amountCurrency())
                .withCommission(extractedData.commission())
                .withCommissionCurrency(extractedData.commissionCurrency())
                .withUploadedFile(receiptPaymentVerificationRequest.file().getBytes());
        return (ReceiptPaymentVerificationPojo) builder.build();
    }

}
