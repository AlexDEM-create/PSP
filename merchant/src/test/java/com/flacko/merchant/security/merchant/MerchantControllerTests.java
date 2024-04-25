package com.flacko.merchant.security.merchant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flacko.merchant.MerchantService;
import com.flacko.merchant.rest.MerchantCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MerchantControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MerchantService merchantService;

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListMerchants() throws Exception {
        merchantService.create()
                .withName("testMerchant1")
                .withUserId("testUser1")
                .withIncomingFeeRate(0.05)
                .withOutgoingFeeRate(0.05)
                .build();
        merchantService.create()
                .withName("testMerchant2")
                .withUserId("testUser2")
                .withIncomingFeeRate(0.05)
                .withOutgoingFeeRate(0.05)
                .build();

        this.mockMvc.perform(get("/merchants"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMerchant() throws Exception {
        this.mockMvc.perform(get("/merchants/{merchantId}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateMerchant() throws Exception {
        MerchantCreateRequest request = new MerchantCreateRequest("name", "userId", BigDecimal.TEN, BigDecimal.TEN);
        this.mockMvc.perform(post("/merchants").content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testArchiveMerchant() throws Exception {
        this.mockMvc.perform(delete("/merchants/{merchantId}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundException() throws Exception {
        this.mockMvc.perform(get("/merchants/{invalidId}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBadRequestException() throws Exception {
        MerchantCreateRequest request = new MerchantCreateRequest("", "userId", BigDecimal.TEN, BigDecimal.TEN);
        this.mockMvc.perform(post("/merchants").content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}