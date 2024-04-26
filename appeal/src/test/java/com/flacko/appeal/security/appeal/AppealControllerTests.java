package com.flacko.appeal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppealControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppealService appealService;

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListAppeals() throws Exception {
        this.mockMvc.perform(get("/appeals"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAppeal() throws Exception {
        this.mockMvc.perform(get("/appeals/{appealId}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateAppeal() throws Exception {
        AppealCreateRequest request = new AppealCreateRequest("paymentId");
        this.mockMvc.perform(post("/appeals").content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testResolveAppeal() throws Exception {
        this.mockMvc.perform(post("/appeals/{appealId}/resolve"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRejectAppeal() throws Exception {
        this.mockMvc.perform(post("/appeals/{appealId}/reject"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundException() throws Exception {
        this.mockMvc.perform(get("/appeals/{invalidId}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBadRequestException() throws Exception {
        AppealCreateRequest request = new AppealCreateRequest("");
        this.mockMvc.perform(post("/appeals").content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
