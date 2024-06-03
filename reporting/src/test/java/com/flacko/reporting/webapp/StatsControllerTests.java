package com.flacko.reporting.webapp;

import com.flacko.balance.service.BalanceService;
import com.flacko.balance.service.BalanceType;
import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.payment.RecipientPaymentMethodType;
import com.flacko.common.role.UserRole;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.reporting.service.StatsService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeam;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import com.flacko.reporting.impl.StatsServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.flacko.reporting.service.EntityType;
import com.flacko.reporting.service.Stats;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StatsControllerTests {

    @Autowired
    private StatsService statsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OutgoingPaymentService outgoingPaymentService;
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

    @Autowired
    private TerminalService terminalService;


    private String outgoingPaymentId;
    private String merchantId;
    private String traderTeamId;
    private String paymentMethodId;
    private BigDecimal traderOutgoingFeeRate;
    private BigDecimal leaderOutgoingFeeRate;

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

        String traderTeamUserId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();


        String traderTeamLeaderId = userService.create()
                .withLogin(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        TraderTeam traderTeam = traderTeamService.create()
                .withName(org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(10))
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


        String terminalId = terminalService.create()
                .withTraderTeamId(traderTeamId)
                .withVerified()
                .withModel("Xiaomi")
                .withOperatingSystem("Android")
                .build()
                .getId();

        paymentMethodId = paymentMethodService.create()
                .withNumber("1234567812345678")
                .withFirstName("John")
                .withLastName("Grey")
                .withCurrency(Currency.RUB)
                .withBank(Bank.SBER)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .withEnabled(true)
                .build()
                .getId();


         outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(55000))
                .withCurrency(Currency.RUB)
                .withRecipient("1234 5678 1234 9398")
                .withBank(Bank.SBER)
                .withRecipientPaymentMethodType(RecipientPaymentMethodType.BANK_CARD)
                .withPartnerPaymentId("test_partner_payment_id")
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        outgoingPaymentService.update(outgoingPaymentId)
                .withState(PaymentState.FAILED_TO_VERIFY)
                .build();

    }

    @Test
    public void testGetStatsForTraderTeam() throws Exception {
        DecimalFormat df = new DecimalFormat("#.0", new DecimalFormatSymbols(Locale.US));
        df.setRoundingMode(RoundingMode.HALF_UP);

        Stats stats = statsService.create()
                .withEntityId(traderTeamId)
                .withEntityType(EntityType.TRADER_TEAM)
                .build();

        mockMvc.perform(get("/stats/trader-teams/" + stats.getEntityId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(stats.getId()))
                .andExpect(jsonPath("$.entity_id").value(stats.getEntityId()))
                .andExpect(jsonPath("$.entity_type").value(stats.getEntityType().toString()))
                .andExpect(jsonPath("$.today_outgoing_total").value(Double.parseDouble(df.format(stats.getTodayOutgoingTotal()))))
                .andExpect(jsonPath("$.today_incoming_total").value(Double.parseDouble(df.format(stats.getTodayIncomingTotal()))))
                .andExpect(jsonPath("$.all_time_outgoing_total").value(Double.parseDouble(df.format(stats.getAllTimeOutgoingTotal()))))
                .andExpect(jsonPath("$.all_time_incoming_total").value(Double.parseDouble(df.format(stats.getAllTimeIncomingTotal()))))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());
    }

    @Test
    public void testGetStatsForMerchant() throws Exception {
        DecimalFormat df = new DecimalFormat("#.0", new DecimalFormatSymbols(Locale.US));
        df.setRoundingMode(RoundingMode.HALF_UP);


        Stats stats = statsService.create()
                .withEntityId(merchantId)
                .withEntityType(EntityType.MERCHANT)
                .build();

        mockMvc.perform(get("/stats/merchants/" + stats.getEntityId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(stats.getId()))
                .andExpect(jsonPath("$.entity_id").value(stats.getEntityId()))
                .andExpect(jsonPath("$.entity_type").value(stats.getEntityType().toString()))
                .andExpect(jsonPath("$.today_outgoing_total").value(Double.parseDouble(df.format(stats.getTodayOutgoingTotal()))))
                .andExpect(jsonPath("$.today_incoming_total").value(Double.parseDouble(df.format(stats.getTodayIncomingTotal()))))
                .andExpect(jsonPath("$.all_time_outgoing_total").value(Double.parseDouble(df.format(stats.getAllTimeOutgoingTotal()))))
                .andExpect(jsonPath("$.all_time_incoming_total").value(Double.parseDouble(df.format(stats.getAllTimeIncomingTotal()))))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());
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
