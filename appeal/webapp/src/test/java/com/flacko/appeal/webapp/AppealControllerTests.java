package com.flacko.appeal.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealService;
import com.flacko.appeal.service.AppealSource;
import com.flacko.appeal.service.AppealState;
import com.flacko.appeal.service.exception.AppealIllegalPaymentCurrentStateException;
import com.flacko.appeal.service.exception.AppealIllegalStateTransitionException;
import com.flacko.appeal.service.exception.AppealMissingRequiredAttributeException;
import com.flacko.appeal.webapp.rest.AppealCreateRequest;
import com.flacko.bank.service.BankService;
import com.flacko.card.service.CardService;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.service.PaymentDirection;
import com.flacko.payment.service.PaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.UserRole;
import com.flacko.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppealControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppealService appealService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TraderTeamService traderTeamService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankService bankService;

    @Autowired
    private TerminalService terminalService;

    private String paymentId;

    @BeforeEach
    public void setup() throws Exception {
        String merchantUserId = userService.create()
                .withLogin("test_merchant")
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build()
                .getId();

        String merchantId = merchantService.create()
                .withName("test_merchant")
                .withUserId(merchantUserId)
                .withIncomingFeeRate(BigDecimal.valueOf(0.02))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.02))
                .build()
                .getId();

        String traderTeamUserId = userService.create()
                .withLogin("test_trader_team")
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();

        String traderTeamLeaderId = userService.create()
                .withLogin("test_trader_team_leader")
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        String traderTeamId = traderTeamService.create()
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
                .withCountry("Russia")
                .build()
                .getId();

        String terminalId = terminalService.create()
                .withTraderTeamId(traderTeamId)
                .withVerified()
                .withModel("Xiaomi")
                .withOperatingSystem("Android")
                .build()
                .getId();

        String cardId = cardService.create()
                .withNumber("1234567812345678")
                .withBankId(bankId)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .build()
                .getId();

        paymentId = paymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withCardId(cardId)
                .withAmount(BigDecimal.valueOf(5000))
                .withCurrency(Currency.RUB)
                .withDirection(PaymentDirection.INCOMING)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();

        paymentService.update(paymentId)
                .withState(PaymentState.FAILED_TO_VERIFY)
                .build();
    }

    @Test
    public void testListAppeals() throws Exception {
        Appeal appeal1 = appealService.create()
                .withPaymentId(paymentId)
                .withSource(AppealSource.TRADER_TEAM)
                .build();
        Appeal appeal2 = appealService.create()
                .withPaymentId(paymentId)
                .withSource(AppealSource.MERCHANT)
                .withState(AppealState.UNDER_REVIEW)
                .build();

        mockMvc.perform(get("/appeals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(appeal1.getId()))
                .andExpect(jsonPath("$[0].payment_id").value(appeal1.getPaymentId()))
                .andExpect(jsonPath("$[0].source").value(appeal1.getSource().toString()))
                .andExpect(jsonPath("$[0].current_state").value(appeal1.getCurrentState().toString()))
                .andExpect(jsonPath("$[0].created_date").isNotEmpty())
                .andExpect(jsonPath("$[0].updated_date").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(appeal2.getId()))
                .andExpect(jsonPath("$[1].payment_id").value(appeal2.getPaymentId()))
                .andExpect(jsonPath("$[1].source").value(appeal2.getSource().toString()))
                .andExpect(jsonPath("$[1].current_state").value(appeal2.getCurrentState().toString()))
                .andExpect(jsonPath("$[1].created_date").isNotEmpty())
                .andExpect(jsonPath("$[1].updated_date").isNotEmpty());
    }
    @Test
    public void testGetAppeal() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId(paymentId)
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        mockMvc.perform(get("/appeals/" + appeal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(appeal.getId()))
                .andExpect(jsonPath("$.payment_id").value(appeal.getPaymentId()))
                .andExpect(jsonPath("$.source").value(appeal.getSource().toString()))
                .andExpect(jsonPath("$.current_state").value(appeal.getCurrentState().toString()))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());

    }
    @Test
    public void testGetAppealNotFound() throws Exception {
        String nonExistentAppealId = "nonExistentAppealId";

        mockMvc.perform(get("/appeals/" + nonExistentAppealId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAppeal() throws Exception {
        AppealCreateRequest request = new AppealCreateRequest(paymentId, AppealSource.TRADER_TEAM);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.payment_id").value(request.paymentId()))
                .andExpect(jsonPath("$.source").value(request.source()))
                .andExpect(jsonPath("$.current_state").value(AppealState.INITIATED.toString()))
                .andExpect(jsonPath("$.created_date").isNotEmpty())
                .andExpect(jsonPath("$.updated_date").isNotEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTest")
    public void testCreateAppealThrowsAppealMissingRequiredAttributeException(String input) throws Exception {
        AppealCreateRequest request = new AppealCreateRequest(input, AppealSource.TRADER_TEAM);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

        private static Stream<String> provideStringsForTest() {
            return Stream.of("", " ", null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreateAppeal_ThrowsAppealMissingRequiredAttributeException_InvalidSource(AppealSource source)
            throws Exception {
        AppealCreateRequest request = new AppealCreateRequest("validPaymentId", source);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateAppealThrowsPaymentNotFoundException() throws Exception {
        AppealCreateRequest request = new AppealCreateRequest("nonexistentPaymentId", AppealSource.TRADER_TEAM);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testResolveAppeal() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId(paymentId)
                .withSource(AppealSource.TRADER_TEAM)
                .build();
        this.mockMvc.perform(post("/appeals/{appealId}/resolve", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value(AppealState.RESOLVED.toString()));

    }
    @Test
    public void testResolveAppealThrowsAppealIllegalPaymentCurrentStateException() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        doThrow(new AppealIllegalPaymentCurrentStateException(appeal.getId(), appeal.getPaymentId(), PaymentState.FAILED_TO_VERIFY))
                .when(appealService).update(appeal.getId());

        this.mockMvc.perform(post("/appeals/{appealId}/resolve", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testResolveAppealThrowsPaymentNotFoundException() throws Exception {
        this.mockMvc.perform(post("/appeals/{appealId}/resolve", "invalidAppealId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testResolveAppeal_ThrowsAppealIllegalStateTransitionException() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        appealService.update(appeal.getId())
                .withState(AppealState.REJECTED)
                .build();

        this.mockMvc.perform(post("/appeals/{appealId}/resolve", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testResolveAppeal_Exception() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        this.mockMvc.perform(post("/appeals/{appealId}/resolve", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AppealMissingRequiredAttributeException));
    }
    @Test
    public void testAppealNotFoundException() throws Exception {
        this.mockMvc.perform(get("/appeals/{appealId}", "invalidAppealId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRejectAppeal() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId(paymentId)
                .withSource(AppealSource.TRADER_TEAM)
                .build();
        this.mockMvc.perform(post("/appeals/{appealId}/reject", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value(AppealState.REJECTED.toString()));
    }

    @Test
    public void testRejectAppealThrowsAppealIllegalPaymentCurrentStateException() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        doThrow(new AppealIllegalPaymentCurrentStateException(appeal.getId(), appeal.getPaymentId(), PaymentState.FAILED_TO_VERIFY))
                .when(appealService).update(appeal.getId());

        this.mockMvc.perform(post("/appeals/{appealId}/reject", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRejectAppealThrowsPaymentNotFoundException() throws Exception {
        this.mockMvc.perform(post("/appeals/{appealId}/reject", "invalidAppealId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRejectAppeal_ThrowsAppealIllegalStateTransitionException() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        appealService.update(appeal.getId())
                .withState(AppealState.RESOLVED)
                .build();

        this.mockMvc.perform(post("/appeals/{appealId}/reject", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRejectAppeal_Exception() throws Exception {
        Appeal appeal = appealService.create()
                .withPaymentId("testPaymentId")
                .withSource(AppealSource.TRADER_TEAM)
                .build();

        this.mockMvc.perform(post("/appeals/{appealId}/reject", appeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AppealMissingRequiredAttributeException));
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
