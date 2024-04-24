package com.flacko.merchant.security.merchant;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.merchant.MerchantRepository;
import com.flacko.merchant.MerchantService;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private MerchantService merchantService;

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
    public void testListMerchants() throws Exception {
        merchantService.create()
                .withName("testMerchant1")
                .withUserId("testUser1")
                .withIncomingFeeRate(BigDecimal.valueOf(0.05))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.05))
                .build();
        merchantService.create()
                .withName("testMerchant2")
                .withUserId("testUser2")
                .withIncomingFeeRate(BigDecimal.valueOf(0.05))
                .withOutgoingFeeRate(BigDecimal.valueOf(0.05))
                .build();

        mockMvc.perform(get("/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", is("testMerchant1")))
                .andExpect(jsonPath("$[0].userId", is("testUser1")))
                .andExpect(jsonPath("$[0].incomingFeeRate", is(0.05)))
                .andExpect(jsonPath("$[0].outgoingFeeRate", is(0.05)))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].name", is("testMerchant2")))
                .andExpect(jsonPath("$[1].userId", is("testUser2")))
                .andExpect(jsonPath("$[1].incomingFeeRate", is(0.05)))
                .andExpect(jsonPath("$[1].outgoingFeeRate", is(0.05)));
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