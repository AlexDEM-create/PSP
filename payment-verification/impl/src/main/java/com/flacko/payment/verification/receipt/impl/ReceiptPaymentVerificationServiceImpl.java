package com.flacko.payment.verification.receipt.impl;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.exception.BalanceInvalidCurrentBalanceException;
import com.flacko.common.exception.BalanceMissingRequiredAttributeException;
import com.flacko.common.exception.BalanceNotFoundException;
import com.flacko.common.exception.IncomingPaymentNotFoundException;
import com.flacko.common.exception.MerchantInsufficientOutgoingBalanceException;
import com.flacko.common.exception.MerchantInvalidFeeRateException;
import com.flacko.common.exception.MerchantMissingRequiredAttributeException;
import com.flacko.common.exception.MerchantNotFoundException;
import com.flacko.common.exception.NoEligibleTraderTeamsException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import com.flacko.common.exception.OutgoingPaymentInvalidAmountException;
import com.flacko.common.exception.OutgoingPaymentMissingRequiredAttributeException;
import com.flacko.common.exception.OutgoingPaymentNotFoundException;
import com.flacko.common.exception.PaymentMethodNotFoundException;
import com.flacko.common.exception.ReceiptPaymentVerificationNotFoundException;
import com.flacko.common.exception.TraderTeamIllegalLeaderException;
import com.flacko.common.exception.TraderTeamInvalidFeeRateException;
import com.flacko.common.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.common.exception.TraderTeamNotAllowedOnlineException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.exception.UnauthorizedAccessException;
import com.flacko.common.exception.UserNotFoundException;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.receipt.ReceiptPattern;
import com.flacko.common.receipt.ReceiptPatternType;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.method.service.PaymentMethod;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPayment;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.payment.verification.receipt.impl.validator.ReceiptValidator;
import com.flacko.payment.verification.receipt.impl.validator.ReceiptValidatorFactory;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationListBuilder;
import com.flacko.payment.verification.receipt.service.ReceiptPaymentVerificationService;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationCurrencyNotSupportedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationFailedException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationInvalidAmountException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationMissingRequiredAttributeException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationRequestValidationException;
import com.flacko.payment.verification.receipt.service.exception.ReceiptPaymentVerificationUnexpectedAmountException;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.flacko.payment.verification.receipt.service.ReceiptPaymentVerification.MAX_RECEIPT_SIZE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptPaymentVerificationServiceImpl implements ReceiptPaymentVerificationService {

    private final ReceiptPaymentVerificationRepository receiptPaymentVerificationRepository;
    private final ServiceLocator serviceLocator;
    private final BalanceService balanceService;
    private final OutgoingPaymentService outgoingPaymentService;
    private final PaymentMethodService paymentMethodService;
    private final TraderTeamService traderTeamService;
    private final RestTemplate restTemplate;
    @Value("${receipt.data.extractor.url}")
    private String receiptDataExtractorUrl;

    @Override
    public ReceiptPaymentVerificationListBuilder list() {
        return serviceLocator.create(ReceiptPaymentVerificationListBuilder.class);
    }

    @Override
    public ReceiptPaymentVerification getByOutgoingPaymentId(String outgoingPaymentId)
            throws ReceiptPaymentVerificationNotFoundException {
        return receiptPaymentVerificationRepository.findByOutgoingPaymentId(outgoingPaymentId)
                .orElseThrow(() -> new ReceiptPaymentVerificationNotFoundException(outgoingPaymentId));
    }

    @Transactional
    @Override
    public ReceiptPaymentVerification verify(MultipartFile file, String outgoingPaymentId, String paymentMethodId)
            throws ReceiptPaymentVerificationRequestValidationException, ReceiptPaymentVerificationFailedException,
            ReceiptPaymentVerificationCurrencyNotSupportedException, IncomingPaymentNotFoundException,
            ReceiptPaymentVerificationMissingRequiredAttributeException,
            ReceiptPaymentVerificationInvalidAmountException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException, PaymentMethodNotFoundException, TraderTeamNotFoundException,
            BalanceNotFoundException, MerchantNotFoundException, BalanceMissingRequiredAttributeException,
            OutgoingPaymentIllegalStateTransitionException, OutgoingPaymentMissingRequiredAttributeException,
            OutgoingPaymentInvalidAmountException, UserNotFoundException, BalanceInvalidCurrentBalanceException,
            MerchantInvalidFeeRateException, MerchantMissingRequiredAttributeException,
            MerchantInsufficientOutgoingBalanceException, TraderTeamMissingRequiredAttributeException,
            TraderTeamNotAllowedOnlineException, UnauthorizedAccessException, TraderTeamInvalidFeeRateException,
            NoEligibleTraderTeamsException, TraderTeamIllegalLeaderException {
        if (file.isEmpty()) {
            throw new ReceiptPaymentVerificationRequestValidationException("Please upload a file.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new ReceiptPaymentVerificationRequestValidationException("Uploaded file is not a PDF.");
        }

        if (file.getSize() > MAX_RECEIPT_SIZE) {
            throw new ReceiptPaymentVerificationRequestValidationException("File size exceeds 256 KB.");
        }

        OutgoingPayment outgoingPayment = outgoingPaymentService.get(outgoingPaymentId);
        PaymentMethod paymentMethod = paymentMethodService.get(paymentMethodId);
        ReceiptPatternType receiptPatternType = getReceiptPatternType(outgoingPayment, paymentMethod);

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path tempDir = Files.createTempDirectory("uploads");
            Path filePath = tempDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String fileAbsolutePath = filePath.toAbsolutePath().toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("file", new FileSystemResource(fileAbsolutePath));
            formData.add("pattern", ReceiptPattern.getPattern(paymentMethod.getBank(), receiptPatternType));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

            ResponseEntity<ReceiptExtractedData> response = restTemplate.postForEntity(
                    receiptDataExtractorUrl,
                    requestEntity,
                    ReceiptExtractedData.class);

            // Клиент указывает, что хочет получить 5000 рублей на сбер по номеру телефона.
            // Трейдер может отправить деньги с любой зарегистрированной карты, просто указывая с какой именно
            // На основе этого мы можем подобрать паттерн для валидации чека
            // Если клиент указывает номер карты, и банк клиента совпадает с банком выбранной трейдером карты, то
            // подгружаем паттерн внутренних платежей
            // Если клиент указывает номер телефона, то выбираем паттерн банка для переводов по СБП
            // Если клиент указывает номер карты, но банки не совпадают, то выбираем паттерн банка трейдера для
            // внешних платежей

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ReceiptExtractedData receiptExtractedData = response.getBody();

                ReceiptValidator receiptValidator =
                        ReceiptValidatorFactory.createValidator(paymentMethod.getBank(), receiptPatternType);
                receiptValidator.validate(outgoingPayment, paymentMethod, receiptExtractedData.getData());

                TraderTeam traderTeam = traderTeamService.get(outgoingPayment.getTraderTeamId());

                balanceService.update(outgoingPayment.getTraderTeamId(), EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .deposit(outgoingPayment.getAmount())
                        .deposit(outgoingPayment.getAmount().multiply(traderTeam.getTraderOutgoingFeeRate()))
                        .build();

                balanceService.update(traderTeam.getLeaderId(), EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .deposit(outgoingPayment.getAmount().multiply(traderTeam.getLeaderOutgoingFeeRate()))
                        .build();

                balanceService.update(outgoingPayment.getMerchantId(), EntityType.MERCHANT, BalanceType.OUTGOING)
                        .withdraw(outgoingPayment.getAmount())
                        .build();

                outgoingPaymentService.update(outgoingPaymentId)
                        .withState(PaymentState.VERIFIED)
                        .withPaymentMethodId(paymentMethodId)
                        .build();

                return createReceiptPaymentVerification(receiptExtractedData, file, outgoingPaymentId);
            } else {
                log.warn(String.format("Outgoing payment %s verification failed. verificationResponse=%s",
                        outgoingPaymentId, response));
                throw new ReceiptPaymentVerificationFailedException(outgoingPaymentId);
            }
        } catch (IOException e) {
            throw new ReceiptPaymentVerificationFailedException(outgoingPaymentId, e);
        } catch (ReceiptPaymentVerificationFailedException e) {
            outgoingPaymentService.update(outgoingPaymentId)
                    .withState(PaymentState.FAILED_TO_VERIFY)
                    .build();

            traderTeamService.update(outgoingPayment.getTraderTeamId())
                    .withKickedOut(true)
                    .build();
            throw e;
        }
    }

    private ReceiptPaymentVerification createReceiptPaymentVerification(
            ReceiptExtractedData extractedData, MultipartFile file, String outgoingPaymentId)
            throws IOException, ReceiptPaymentVerificationMissingRequiredAttributeException,
            IncomingPaymentNotFoundException, ReceiptPaymentVerificationUnexpectedAmountException,
            OutgoingPaymentNotFoundException {
        ReceiptPaymentVerificationBuilder builder =
                serviceLocator.create(InitializableReceiptPaymentVerificationBuilder.class)
                        .initializeNew();
        builder.withOutgoingPaymentId(outgoingPaymentId)
                .withData(extractedData.getData())
                .withUploadedFile(file.getBytes());
        return builder.build();
    }

    private ReceiptPatternType getReceiptPatternType(OutgoingPayment outgoingPayment, PaymentMethod paymentMethod) {
        if (paymentMethod.getBank() == outgoingPayment.getBank()
                && outgoingPayment.getRecipientPaymentMethodType() == RecipientPaymentMethodType.BANK_CARD) {
            return ReceiptPatternType.BANK_CARD_INTERNAL;
        } else if (paymentMethod.getBank() != outgoingPayment.getBank()
                && outgoingPayment.getRecipientPaymentMethodType() == RecipientPaymentMethodType.BANK_CARD) {
            return ReceiptPatternType.BANK_CARD_EXTERNAL;
        } else if (paymentMethod.getBank() == outgoingPayment.getBank()
                && outgoingPayment.getRecipientPaymentMethodType() == RecipientPaymentMethodType.PHONE_NUMBER) {
            return ReceiptPatternType.PHONE_NUMBER_INTERNAL;
        } else if (paymentMethod.getBank() != outgoingPayment.getBank()
                && outgoingPayment.getRecipientPaymentMethodType() == RecipientPaymentMethodType.PHONE_NUMBER) {
            return ReceiptPatternType.PHONE_NUMBER_EXTERNAL;
        }
        throw new IllegalArgumentException(String.format(
                "Could not determine receipt pattern type for outgoing payment %s", outgoingPayment.getId()));
    }

}
