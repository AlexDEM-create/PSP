package com.flacko.payment.verification.webapp;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.balance.service.EntityType;
import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReceiptPaymentVerificationControllerTests {

    private static final BigDecimal AMOUNT_1 = BigDecimal.valueOf(3000);
    private static final BigDecimal AMOUNT_2 = BigDecimal.valueOf(42000);
    private static final BigDecimal AMOUNT_3 = BigDecimal.valueOf(5000);
    private static final BigDecimal AMOUNT_4 = BigDecimal.valueOf(114000);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutgoingPaymentService outgoingPaymentService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TraderTeamService traderTeamService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService userService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private BalanceService balanceService;

    private String merchantId;
    private BigDecimal merchantOutgoingFeeRate;
    private String traderTeamId;
    private String traderTeamLeaderId;
    private BigDecimal traderOutgoingFeeRate;
    private BigDecimal leaderOutgoingFeeRate;
    private String terminalId;
    private User merchantUser;

    @BeforeEach
    public void setup() throws Exception {
         merchantUser = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build();

        Merchant merchant = merchantService.create()
                .withName(RandomStringUtils.randomAlphanumeric(10))
                .withUserId(merchantUser.getId())
                .withCountry(Country.RUSSIA)
                .withIncomingFeeRate(BigDecimal.valueOf(0.05))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.05))
                .withWebhook(new URL("https://httpbin.org/post"))
                .build();
        merchantId = merchant.getId();
        merchantOutgoingFeeRate = merchant.getOutgoingFeeRate();

        String traderTeamUserId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();

        traderTeamLeaderId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        TraderTeam traderTeam = traderTeamService.create()
                .withName(RandomStringUtils.randomAlphanumeric(10))
                .withUserId(traderTeamUserId)
                .withCountry(Country.RUSSIA)
                .withLeaderId(traderTeamLeaderId)
                .withTraderIncomingFeeRate(BigDecimal.valueOf(0.018))
                .withTraderOutgoingFeeRate(BigDecimal.valueOf(0.018))
                .withLeaderIncomingFeeRate(BigDecimal.valueOf(0.002))
                .withLeaderOutgoingFeeRate(BigDecimal.valueOf(0.002))
                .withVerified()
                .withOutgoingOnline(true)
                .build();
        traderTeamId = traderTeam.getId();
        traderOutgoingFeeRate = traderTeam.getTraderOutgoingFeeRate();
        leaderOutgoingFeeRate = traderTeam.getLeaderOutgoingFeeRate();

        terminalId = terminalService.create()
                .withTraderTeamId(traderTeamId)
                .withVerified()
                .withModel("Xiaomi")
                .withOperatingSystem("Android")
                .build()
                .getId();
    }

    @Test
    public void testSberBankCardInternalReceiptPaymentVerification() throws Exception {
        String paymentMethodId = paymentMethodService.create()
                .withNumber("1234567812345678")
                .withFirstName("Данил")
                .withLastName("Петров")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .withEnabled(true)
                .build()
                .getId();

        BigDecimal deposit = BigDecimal.valueOf(60000);
        BigDecimal initialMerchantOutgoingBalance =
                balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .deposit(deposit)
                        .build()
                        .getCurrentBalance();
        assertThat(initialMerchantOutgoingBalance)
                .isEqualByComparingTo(deposit.subtract(deposit.multiply(merchantOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(57000));

        String outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(AMOUNT_1)
                .withCurrency(Currency.RUB)
                .withRecipient("1234 5678 1234 9398")
                .withBank(Bank.SBER)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.BANK_CARD)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        BigDecimal initialTraderTeamBalance =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamBalance).isEqualByComparingTo(BigDecimal.ZERO);

        BigDecimal initialTraderTeamLeaderBalance =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamLeaderBalance).isEqualByComparingTo(BigDecimal.ZERO);

        InputStream inputStream = new ClassPathResource("test/sber_receipt_1.pdf").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "sber_receipt_1.pdf",
                MediaType.APPLICATION_PDF_VALUE, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/payment-verifications/receipts")
                .file(file)
                .param("outgoing_payment_id", outgoingPaymentId)
                .param("payment_method_id", paymentMethodId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(outgoingPaymentService.get(outgoingPaymentId).getCurrentState())
                .isEqualTo(PaymentState.VERIFIED);

        BigDecimal merchantOutgoingBalanceAfterPayment =
                balanceService.get(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .getCurrentBalance();
        assertThat(merchantOutgoingBalanceAfterPayment)
                .isEqualByComparingTo(initialMerchantOutgoingBalance.subtract(AMOUNT_1))
                .isEqualByComparingTo(BigDecimal.valueOf(54000));

        BigDecimal traderTeamBalanceAfterPayment =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_1.add(AMOUNT_1.multiply(traderOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(3054));

        BigDecimal traderTeamLeaderBalanceAfterPayment =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamLeaderBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_1.multiply(leaderOutgoingFeeRate))
                .isEqualByComparingTo(BigDecimal.valueOf(6));
    }

    @Test
    public void testSberPhoneNumberInternalReceiptPaymentVerification() throws Exception {
        String paymentMethodId = paymentMethodService.create()
                .withNumber("1234567812347614")
                .withFirstName("Аминат")
                .withLastName("Петрова")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .withEnabled(true)
                .build()
                .getId();

        BigDecimal deposit = BigDecimal.valueOf(60000);
        BigDecimal initialMerchantOutgoingBalance =
                balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .deposit(deposit)
                        .build()
                        .getCurrentBalance();
        assertThat(initialMerchantOutgoingBalance)
                .isEqualByComparingTo(deposit.subtract(deposit.multiply(merchantOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(57000));

        String outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(AMOUNT_2)
                .withCurrency(Currency.RUB)
                .withRecipient("+79999206465")
                .withBank(Bank.SBER)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.PHONE_NUMBER)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        BigDecimal initialTraderTeamBalance =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamBalance).isEqualByComparingTo(BigDecimal.ZERO);

        BigDecimal initialTraderTeamLeaderBalance =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamLeaderBalance).isEqualByComparingTo(BigDecimal.ZERO);

        InputStream inputStream = new ClassPathResource("test/sber_receipt_11.pdf").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "sber_receipt_11.pdf",
                MediaType.APPLICATION_PDF_VALUE, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/payment-verifications/receipts")
                .file(file)
                .param("outgoing_payment_id", outgoingPaymentId)
                .param("payment_method_id", paymentMethodId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(outgoingPaymentService.get(outgoingPaymentId).getCurrentState())
                .isEqualTo(PaymentState.VERIFIED);

        BigDecimal merchantOutgoingBalanceAfterPayment =
                balanceService.get(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .getCurrentBalance();
        assertThat(merchantOutgoingBalanceAfterPayment)
                .isEqualByComparingTo(initialMerchantOutgoingBalance.subtract(AMOUNT_2))
                .isEqualByComparingTo(BigDecimal.valueOf(15000));

        BigDecimal traderTeamBalanceAfterPayment =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_2.add(AMOUNT_2.multiply(traderOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(42756));

        BigDecimal traderTeamLeaderBalanceAfterPayment =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamLeaderBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_2.multiply(leaderOutgoingFeeRate))
                .isEqualByComparingTo(BigDecimal.valueOf(84));
    }

    @Test
    public void testSberPhoneNumberExternalReceiptPaymentVerification() throws Exception {
        String paymentMethodId = paymentMethodService.create()
                .withNumber("1234567812348169")
                .withFirstName("Евгений")
                .withLastName("Петров")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .withEnabled(true)
                .build()
                .getId();

        BigDecimal deposit = BigDecimal.valueOf(60000);
        BigDecimal initialMerchantOutgoingBalance =
                balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .deposit(deposit)
                        .build()
                        .getCurrentBalance();
        assertThat(initialMerchantOutgoingBalance)
                .isEqualByComparingTo(deposit.subtract(deposit.multiply(merchantOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(57000));

        String outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(AMOUNT_3)
                .withCurrency(Currency.RUB)
                .withRecipient("+7 (988) 925-28-21")
                .withBank(Bank.TINKOFF)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.PHONE_NUMBER)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        BigDecimal initialTraderTeamBalance =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamBalance).isEqualByComparingTo(BigDecimal.ZERO);

        BigDecimal initialTraderTeamLeaderBalance =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamLeaderBalance).isEqualByComparingTo(BigDecimal.ZERO);

        InputStream inputStream = new ClassPathResource("test/sber_receipt_12.pdf").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "sber_receipt_12.pdf",
                MediaType.APPLICATION_PDF_VALUE, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/payment-verifications/receipts")
                .file(file)
                .param("outgoing_payment_id", outgoingPaymentId)
                .param("payment_method_id", paymentMethodId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(outgoingPaymentService.get(outgoingPaymentId).getCurrentState())
                .isEqualTo(PaymentState.VERIFIED);

        BigDecimal merchantOutgoingBalanceAfterPayment =
                balanceService.get(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .getCurrentBalance();
        assertThat(merchantOutgoingBalanceAfterPayment)
                .isEqualByComparingTo(initialMerchantOutgoingBalance.subtract(AMOUNT_3))
                .isEqualByComparingTo(BigDecimal.valueOf(52000));

        BigDecimal traderTeamBalanceAfterPayment =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_3.add(AMOUNT_3.multiply(traderOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(5090));

        BigDecimal traderTeamLeaderBalanceAfterPayment =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamLeaderBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_3.multiply(leaderOutgoingFeeRate))
                .isEqualByComparingTo(BigDecimal.valueOf(10));
    }

    @Test
    public void testSberBankCardExternalReceiptPaymentVerification() throws Exception {
        String paymentMethodId = paymentMethodService.create()
                .withNumber("1234567812346614")
                .withFirstName("Анна")
                .withLastName("Петрова")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .withEnabled(true)
                .build()
                .getId();

        BigDecimal deposit = BigDecimal.valueOf(150000);
        BigDecimal initialMerchantOutgoingBalance =
                balanceService.update(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .deposit(deposit)
                        .build()
                        .getCurrentBalance();
        assertThat(initialMerchantOutgoingBalance)
                .isEqualByComparingTo(deposit.subtract(deposit.multiply(merchantOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(142500));

        String outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(AMOUNT_4)
                .withCurrency(Currency.RUB)
                .withRecipient("1234 5678 1234 4354")
                .withBank(Bank.VTB)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.BANK_CARD)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        BigDecimal initialTraderTeamBalance =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamBalance).isEqualByComparingTo(BigDecimal.ZERO);

        BigDecimal initialTraderTeamLeaderBalance =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(initialTraderTeamLeaderBalance).isEqualByComparingTo(BigDecimal.ZERO);

        InputStream inputStream = new ClassPathResource("test/sber_receipt_10.pdf").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file", "sber_receipt_10.pdf",
                MediaType.APPLICATION_PDF_VALUE, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/payment-verifications/receipts")
                .file(file)
                .param("outgoing_payment_id", outgoingPaymentId)
                .param("payment_method_id", paymentMethodId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(outgoingPaymentService.get(outgoingPaymentId).getCurrentState())
                .isEqualTo(PaymentState.VERIFIED);

        BigDecimal merchantOutgoingBalanceAfterPayment =
                balanceService.get(merchantId, EntityType.MERCHANT, BalanceType.OUTGOING)
                        .getCurrentBalance();
        assertThat(merchantOutgoingBalanceAfterPayment)
                .isEqualByComparingTo(initialMerchantOutgoingBalance.subtract(AMOUNT_4))
                .isEqualByComparingTo(BigDecimal.valueOf(28500));

        BigDecimal traderTeamBalanceAfterPayment =
                balanceService.get(traderTeamId, EntityType.TRADER_TEAM, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_4.add(AMOUNT_4.multiply(traderOutgoingFeeRate)))
                .isEqualByComparingTo(BigDecimal.valueOf(116052));

        BigDecimal traderTeamLeaderBalanceAfterPayment =
                balanceService.get(traderTeamLeaderId, EntityType.TRADER_TEAM_LEADER, BalanceType.GENERIC)
                        .getCurrentBalance();
        assertThat(traderTeamLeaderBalanceAfterPayment)
                .isEqualByComparingTo(AMOUNT_4.multiply(leaderOutgoingFeeRate))
                .isEqualByComparingTo(BigDecimal.valueOf(228));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
            return MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }
    }

}
