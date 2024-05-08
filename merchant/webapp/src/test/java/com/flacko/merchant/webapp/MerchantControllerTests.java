package com.flacko.merchant.webapp;

import com.flacko.appeal.service.AppealService;
import com.flacko.bank.service.BankService;
import com.flacko.common.country.Country;
import com.flacko.merchant.service.Merchant;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.PaymentService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankService bankService;

    @Autowired
    private TerminalService terminalService;

    private String merchantUserId1;
    private String merchantUserId2;

    @BeforeEach
    public void setup() throws Exception {
        merchantUserId1 = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build()
                .getId();

        merchantUserId2 = userService.create()
                .withLogin(RandomStringUtils.randomAlphanumeric(10))
                .withPassword("qwerty123456")
                .withRole(UserRole.MERCHANT)
                .build()
                .getId();
    }

    @Test
    public void testListMerchants() throws Exception {
        Merchant merchant1 = merchantService.create()
                .withName("test_merchant1")
                .withUserId(merchantUserId1)
                .withCountry(Country.RUSSIA)
                .withIncomingFeeRate(BigDecimal.valueOf(0.02))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.02))
                .build();
        Merchant merchant2 = merchantService.create()
                .withName("test_merchant2")
                .withUserId(merchantUserId2)
                .withCountry(Country.UZBEKISTAN)
                .withIncomingFeeRate(BigDecimal.valueOf(0.03))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.03))
                .build();

        mockMvc.perform(get("/merchants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(merchant1.getId()))
                .andExpect(jsonPath("$[0].name").value(merchant1.getName()))
                .andExpect(jsonPath("$[0].user_id").value(merchant1.getUserId()))
                .andExpect(jsonPath("$[0].incoming_fee_rate").value(merchant1.getIncomingFeeRate()))
                .andExpect(jsonPath("$[0].outgoing_fee_rate").value(merchant1.getOutgoingFeeRate()))
                .andExpect(jsonPath("$[1].id").value(merchant2.getId()))
                .andExpect(jsonPath("$[1].name").value(merchant2.getName()))
                .andExpect(jsonPath("$[1].user_id").value(merchant2.getUserId()))
                .andExpect(jsonPath("$[1].incoming_fee_rate").value(merchant2.getIncomingFeeRate()))
                .andExpect(jsonPath("$[1].outgoing_fee_rate").value(merchant2.getOutgoingFeeRate()));
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
