package com.flacko.payment.webapp;

import com.flacko.bank.service.BankService;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.method.service.PaymentMethodType;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.user.service.UserRole;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IncomingPaymentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IncomingPaymentService incomingPaymentService;

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

        String paymentMethodId = paymentMethodService.create()
                .withType(PaymentMethodType.BANK_CARD)
                .withNumber("1234567812345678")
                .withHolderName("John Grey")
                .withCurrency(Currency.RUB)
                .withBankId(bankId)
                .withTraderTeamId(traderTeamId)
                .withTerminalId(terminalId)
                .build()
                .getId();

        paymentId = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId)
                .withPaymentMethodId(paymentMethodId)
                .withAmount(BigDecimal.valueOf(5000))
                .withCurrency(Currency.RUB)
                .withState(PaymentState.VERIFYING)
                .build()
                .getId();
    }

    @Test
    public void testListIncomingPayments() {

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
