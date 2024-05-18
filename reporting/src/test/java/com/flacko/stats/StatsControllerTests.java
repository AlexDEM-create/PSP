package com.flacko.stats;

import com.flacko.appeal.service.AppealService;
import com.flacko.bank.service.BankService;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.method.service.PaymentMethodType;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import impl.StatsServiceImpl;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.incoming.IncomingPayment;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import service.EntityType;
import service.Stats;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class StatsControllerTests {

    @InjectMocks
    private StatsServiceImpl statsService;

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
    private BankService bankService;

    @Autowired
    private TerminalService terminalService;

    private String incomingPaymentId;
    private String outgoingPaymentId;
    private String merchantId;
    private String traderTeamId;
    private String paymentMethodId;

    @BeforeEach
    public void setup() throws Exception {
        String merchantUserId = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build()
                .getId();

        merchantId = merchantService.create()
                .withName("test_merchant")
                .withUserId(merchantUserId)
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

        String bankId = bankService.create()
                .withName("test_bank")
                .withCountry(Country.RUSSIA)
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
                .withType(PaymentMethodType.BANK_CARD)
                .withNumber("1234567812345678")
                .withHolderName("John Grey")
                .withCurrency(Currency.RUB)
                .withBankId(bankId)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .build()
                .getId();

        String incomingPaymentId1 = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(5000))
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        incomingPaymentService.update(incomingPaymentId1)
                .withState(PaymentState.FAILED_TO_VERIFY)
                .build();

        String incomingPaymentId2 = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(6000)) // Измененная сумма
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        incomingPaymentService.update(incomingPaymentId2)
                .withState(PaymentState.VERIFIED) // Измененный статус
                .build();


        String incomingPaymentId3 = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(7000)) // Измененная сумма
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        incomingPaymentService.update(incomingPaymentId3)
                .withState(PaymentState.VERIFIED) // Измененный статус
                .build();


        outgoingPaymentId = outgoingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
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

}
