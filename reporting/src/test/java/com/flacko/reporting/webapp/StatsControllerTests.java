package com.flacko.reporting.webapp;

import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.terminal.service.TerminalService;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StatsControllerTests {

    @InjectMocks
    private StatsServiceImpl statsService;

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

    private String outgoingPaymentId;
    private String merchantId;
    private String traderTeamId;
    private String paymentMethodId;

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

        String traderTeamUserId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();

        String traderTeamLeaderId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        traderTeamId = traderTeamService.create()
                .withName("test_merchant")
                .withUserId(traderTeamUserId)
                .withLeaderId(traderTeamLeaderId)
                .withTraderIncomingFeeRate(BigDecimal.valueOf(0.018))
                .withTraderOutgoingFeeRate(BigDecimal.valueOf(0.018))
                .withLeaderIncomingFeeRate(BigDecimal.valueOf(0.002))
                .withLeaderOutgoingFeeRate(BigDecimal.valueOf(0.002))
                .build()
                .getId();

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
                .build()
                .getId();

        outgoingPaymentId = outgoingPaymentService.create(merchantUser.getLogin())
                .withRandomTraderTeamId()
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(10000))
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        outgoingPaymentService.update(outgoingPaymentId)
                .withState(PaymentState.FAILED_TO_VERIFY)
                .build();
    }

    @Test
    public void testGetStatsForTraderTeam() throws Exception {
        Stats stats = statsService.create()
                .withEntityId(traderTeamId)
                .withEntityType(EntityType.TRADER_TEAM)
                .build();

        mockMvc.perform(get("/stats/trader-teams/" + stats.getEntityId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.id").value(stats.getId()))
                .andExpect((ResultMatcher) jsonPath("$.entity_id").value(stats.getEntityId()))
                .andExpect((ResultMatcher) jsonPath("$.entity_type").value(stats.getEntityType().toString()))
                .andExpect((ResultMatcher) jsonPath("$.today_outgoing_total").value(stats.getTodayOutgoingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.today_incoming_total").value(stats.getTodayIncomingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.all_time_outgoing_total").value(stats.getAllTimeOutgoingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.all_time_incoming_total").value(stats.getAllTimeIncomingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.created_date").isNotEmpty())
                .andExpect((ResultMatcher) jsonPath("$.updated_date").isNotEmpty());
    }

    @Test
    public void testGetStatsForMerchant() throws Exception {
        Stats stats = statsService.create()
                .withEntityId(merchantId)
                .withEntityType(EntityType.MERCHANT)
                .build();

        mockMvc.perform(get("/stats/merchants/" + stats.getEntityId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.id").value(stats.getId()))
                .andExpect((ResultMatcher) jsonPath("$.entity_id").value(stats.getEntityId()))
                .andExpect((ResultMatcher) jsonPath("$.entity_type").value(stats.getEntityType().toString()))
                .andExpect((ResultMatcher) jsonPath("$.today_outgoing_total").value(stats.getTodayOutgoingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.today_incoming_total").value(stats.getTodayIncomingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.all_time_outgoing_total").value(stats.getAllTimeOutgoingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.all_time_incoming_total").value(stats.getAllTimeIncomingTotal()))
                .andExpect((ResultMatcher) jsonPath("$.created_date").isNotEmpty())
                .andExpect((ResultMatcher) jsonPath("$.updated_date").isNotEmpty());
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
