package com.flacko.stats.webapp;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StatsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutgoingPaymentService outgoingPaymentService;

    @Autowired
    private IncomingPaymentService incomingPaymentService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TraderTeamService traderTeamService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService userService;

    private String merchantId;
    private String traderTeamId1;
    private String traderTeamId2;
    private String paymentMethodId1;
    private String paymentMethodId2;
    private BigDecimal traderOutgoingFeeRate1;
    private BigDecimal leaderOutgoingFeeRate1;
    private BigDecimal traderOutgoingFeeRate2;
    private BigDecimal leaderOutgoingFeeRate2;
    private String outgoingPaymentId1;
    private String outgoingPaymentId2;
    private String incomingPaymentId1;

    @BeforeEach
    public void setup() throws Exception {
        User merchantUser = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build();

        merchantId = merchantService.create()
                .withName("test_merchant")
                .withUserId(merchantUser.getId())
                .withCountry(Country.RUSSIA)
                .withIncomingFeeRate(BigDecimal.valueOf(0.02))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.02))
                .build()
                .getId();

        BigDecimal deposit = BigDecimal.valueOf(150000);
        BigDecimal initialMerchantOutgoingBalance =
                balanceService.update(merchantId, com.flacko.balance.service.EntityType.MERCHANT, BalanceType.OUTGOING)
                        .deposit(deposit)
                        .build()
                        .getCurrentBalance();

        String traderTeamUserId1 = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();

        String traderTeamUserId2 = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("asdfgh123456789")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();

        String traderTeamLeaderId1 = userService.create()
                .withLogin(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty0000001")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        String traderTeamLeaderId2 = userService.create()
                .withLogin(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty0000002")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        TraderTeam traderTeam1 = traderTeamService.create()
                .withName(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
                .withUserId(traderTeamUserId1)
                .withCountry(Country.RUSSIA)
                .withLeaderId(traderTeamLeaderId1)
                .withTraderIncomingFeeRate(BigDecimal.valueOf(0.018))
                .withTraderOutgoingFeeRate(BigDecimal.valueOf(0.018))
                .withLeaderIncomingFeeRate(BigDecimal.valueOf(0.002))
                .withLeaderOutgoingFeeRate(BigDecimal.valueOf(0.002))
                .withVerified()
                .withOutgoingOnline(true)
                .build();
        traderTeamId1 = traderTeam1.getId();
        traderOutgoingFeeRate1 = traderTeam1.getTraderOutgoingFeeRate();
        leaderOutgoingFeeRate1 = traderTeam1.getLeaderOutgoingFeeRate();

        TraderTeam traderTeam2 = traderTeamService.create()
                .withName(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
                .withUserId(traderTeamUserId2)
                .withCountry(Country.RUSSIA)
                .withLeaderId(traderTeamLeaderId2)
                .withTraderIncomingFeeRate(BigDecimal.valueOf(0.021))
                .withTraderOutgoingFeeRate(BigDecimal.valueOf(0.021))
                .withLeaderIncomingFeeRate(BigDecimal.valueOf(0.003))
                .withLeaderOutgoingFeeRate(BigDecimal.valueOf(0.003))
                .withVerified()
                .withOutgoingOnline(true)
                .build();
        traderTeamId2 = traderTeam2.getId();
        traderOutgoingFeeRate2 = traderTeam2.getTraderOutgoingFeeRate();
        leaderOutgoingFeeRate2 = traderTeam2.getLeaderOutgoingFeeRate();

        paymentMethodId1 = paymentMethodService.create()
                .withNumber("1234567812345678")
                .withFirstName("John")
                .withLastName("Grey")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId1)
                .withEnabled(true)
                .build()
                .getId();

        paymentMethodId2 = paymentMethodService.create()
                .withNumber("1234567812345678")
                .withFirstName("Vasiliy")
                .withLastName("Ivanov")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId2)
                .withEnabled(true)
                .build()
                .getId();

        outgoingPaymentId1 = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId(Optional.empty())
                .withPaymentMethodId(paymentMethodId1)
                .withAmount(BigDecimal.valueOf(5000))
                .withCurrency(Currency.RUB)
                .withRecipient("1234 5678 1234 9398")
                .withBank(Bank.SBER)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.BANK_CARD)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        outgoingPaymentService.update(outgoingPaymentId1)
                .withState(PaymentState.VERIFIED)
                .build();

        outgoingPaymentId2 = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId(Optional.empty())
                .withPaymentMethodId(paymentMethodId2)
                .withAmount(BigDecimal.valueOf(3000))
                .withCurrency(Currency.RUB)
                .withRecipient("9398 1234 5678 1234 ")
                .withBank(Bank.SBER)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.BANK_CARD)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        outgoingPaymentService.update(outgoingPaymentId2)
                .withState(PaymentState.VERIFIED)
                .build();

        incomingPaymentId1 = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId1)
                .withPaymentMethodId(paymentMethodId1)
                .withAmount(BigDecimal.valueOf(3300))
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        incomingPaymentService.update(incomingPaymentId1)
                .withState(PaymentState.VERIFIED)
                .build();
    }

    @Test
    public void testGetStatsForTraderTeam() throws Exception {
        Thread.sleep(5000);
        mockMvc.perform(get("/stats/trader-teams/{traderTeamId}", traderTeamId1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.entity_id").exists())
                .andExpect(jsonPath("$.entity_type").exists())
                .andExpect(jsonPath("$.today_outgoing_total").value(5000))
                .andExpect(jsonPath("$.today_incoming_total").value(3300))
                .andExpect(jsonPath("$.all_time_outgoing_total").value(5000))
                .andExpect(jsonPath("$.all_time_incoming_total").value(3300))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());
    }

    @Test
    public void testGetStatsForMerchant() throws Exception {
        Thread.sleep(5000);
        mockMvc.perform(MockMvcRequestBuilders.get("/stats/merchants/{merchantId}", merchantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.entity_id").exists())
                .andExpect(jsonPath("$.entity_type").exists())
                .andExpect(jsonPath("$.today_outgoing_total").value(8000))
                .andExpect(jsonPath("$.today_incoming_total").value(3300))
                .andExpect(jsonPath("$.all_time_outgoing_total").value(8000))
                .andExpect(jsonPath("$.all_time_incoming_total").value(3300))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
            return MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(SecurityMockMvcConfigurers.springSecurity())
                    .build();
        }

    }

}
