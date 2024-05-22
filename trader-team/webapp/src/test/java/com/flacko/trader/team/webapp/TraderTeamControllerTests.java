package com.flacko.trader.team.webapp;

import com.flacko.appeal.service.AppealService;
import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeam;

import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TraderTeamControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppealService appealService;

    @Autowired
    private IncomingPaymentService incomingPaymentService;

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

    private String incomingPaymentId;
    private String outgoingPaymentId;
    private String merchantLogin;
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
        merchantLogin = merchantUser.getLogin();

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

        incomingPaymentId = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(5000))
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        incomingPaymentService.update(incomingPaymentId)
                .withState(PaymentState.FAILED_TO_VERIFY)
                .build();

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
    public void testListTraderTeams() throws Exception {
        // Создание тестовых данных с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId("User1")
                .withLeaderId("Leader1")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        TraderTeam team2 = traderTeamService.create()
                .withName("TestTeam2")
                .withUserId("User2")
                .withLeaderId("Leader2")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        TraderTeam team3 = traderTeamService.create()
                .withName("TestTeam3")
                .withUserId("User3")
                .withLeaderId("Leader3")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        TraderTeam team4 = traderTeamService.create()
                .withName("TestTeam4")
                .withUserId("User4")
                .withLeaderId("Leader4")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        TraderTeam team5 = traderTeamService.create()
                .withName("TestTeam5")
                .withUserId("User5")
                .withLeaderId("Leader5")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        mockMvc.perform(get("/trader-teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[0].name",is("TestTeam1")))
                .andExpect((ResultMatcher) jsonPath("$[0].userId",is("User1")))
                .andExpect((ResultMatcher) jsonPath("$[0].leaderId",is("Leader1")))
                .andExpect(jsonPath("$[1].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[1].name",is("TestTeam2")))
                .andExpect((ResultMatcher) jsonPath("$[1].userId",is("User2")))
                .andExpect((ResultMatcher) jsonPath("$[1].leaderId",is("Leader2")))
                .andExpect(jsonPath("$[2].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[2].name",is("TestTeam3")))
                .andExpect((ResultMatcher) jsonPath("$[2].userId",is("User3")))
                .andExpect((ResultMatcher) jsonPath("$[2].leaderId",is("Leader3")))
                .andExpect(jsonPath("$[3].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[3].name",is("TestTeam4")))
                .andExpect((ResultMatcher) jsonPath("$[3].userId",is("User4")))
                .andExpect((ResultMatcher) jsonPath("$[3].leaderId",is("Leader4")))
                .andExpect(jsonPath("$[4].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[4].name",is("TestTeam5")))
                .andExpect((ResultMatcher) jsonPath("$[4].userId",is("User5")))
                .andExpect((ResultMatcher) jsonPath("$[4].leaderId",is("Leader5")));
        verify(traderTeamService, times(1)).list();
    }
    @Test
    public void testGetTraderTeamById() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId("User1")
                .withLeaderId("Leader1")
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        // Выполнение GET-запроса к эндпоинту и проверка ответа
        mockMvc.perform(get("/trader-teams/" + team1.getId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team1.getId())))
                .andExpect((ResultMatcher) jsonPath("$.name", is("TestTeam1")))
                .andExpect((ResultMatcher) jsonPath("$.userId", is("User1")))
                .andExpect((ResultMatcher) jsonPath("$.leaderId", is("Leader1")));
    }

    @Test
    public void testGetNonexistentTraderTeamById() throws Exception {
        // Выполнение GET-запроса на несуществующий ID команды
        mockMvc.perform(get("/trader-teams/nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamNotFoundException))
                .andExpect(result -> assertEquals("Trader team nonexistent-id not found", result.getResolvedException().getMessage()));
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

