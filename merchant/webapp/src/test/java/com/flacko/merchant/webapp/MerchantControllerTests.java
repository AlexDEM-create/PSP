package com.flacko.merchant.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.merchant.service.MerchantService;
import com.flacko.merchant.webapp.rest.MerchantCreateRequest;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.UserRole;
import com.flacko.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantControllerTests {

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
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
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
    public void testListMerchants() throws Exception {
        Merchant merchant1 = merchantService.create()
                .withName("test_merchant1")
                .withUserId("user1")
                .withIncomingFeeRate(BigDecimal.valueOf(0.02))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.02))
                .build();
        Merchant merchant2 = merchantService.create()
                .withName("test_merchant2")
                .withUserId("user2")
                .withIncomingFeeRate(BigDecimal.valueOf(0.03))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.03))
                .build();

        mockMvc.perform(get("/merchants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(merchant1.getId()))
                .andExpect(jsonPath("$[0].name").value(merchant1.getName()))
                .andExpect(jsonPath("$[0].userId").value(merchant1.getUserId()))
                .andExpect(jsonPath("$[0].incomingFeeRate").value(merchant1.getIncomingFeeRate()))
                .andExpect(jsonPath("$[0].outgoingFeeRate").value(merchant1.getOutgoingFeeRate()))
                .andExpect(jsonPath("$[1].id").value(merchant2.getId()))
                .andExpect(jsonPath("$[1].name").value(merchant2.getName()))
                .andExpect(jsonPath("$[1].userId").value(merchant2.getUserId()))
                .andExpect(jsonPath("$[1].incomingFeeRate").value(merchant2.getIncomingFeeRate()))
                .andExpect(jsonPath("$[1].outgoingFeeRate").value(merchant2.getOutgoingFeeRate()));
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
