package com.flacko.user.webapp;

import com.flacko.common.spring.ServiceLocator;
import com.flacko.common.role.UserRole;
import com.flacko.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceLocator serviceLocator;

    @Test
    public void testListUsers() throws Exception {
        userService.create()
                .withLogin("testUser1")
                .withPassword("testPassword1")
                .withRole(UserRole.USER_ADMIN)
                .build();
        userService.create()
                .withLogin("testUser2")
                .withPassword("testPassword2")
                .withRole(UserRole.USER_SUPPORT)
                .build();
        userService.create()
                .withLogin("testUser3")
                .withPassword("testPassword3")
                .withRole(UserRole.MERCHANT)
                .build();
        userService.create()
                .withLogin("testUser4")
                .withPassword("testPassword4")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build();
        userService.create()
                .withLogin("testUser5")
                .withPassword("testPassword5")
                .withRole(UserRole.TRADER_TEAM)
                .build();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].login", is("testUser1")))
                .andExpect(jsonPath("$[0].role", is(UserRole.USER_ADMIN.name())))
                .andExpect(jsonPath("$[0].banned", is(false)))
                .andExpect(jsonPath("$[0].created_date", notNullValue()))
                .andExpect(jsonPath("$[0].updated_date", notNullValue()))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].login", is("testUser2")))
                .andExpect(jsonPath("$[1].role", is(UserRole.USER_SUPPORT.name())))
                .andExpect(jsonPath("$[1].banned", is(false)))
                .andExpect(jsonPath("$[1].created_date", notNullValue()))
                .andExpect(jsonPath("$[1].updated_date", notNullValue()))
                .andExpect(jsonPath("$[2].id", notNullValue()))
                .andExpect(jsonPath("$[2].login", is("testUser3")))
                .andExpect(jsonPath("$[2].role", is(UserRole.MERCHANT.name())))
                .andExpect(jsonPath("$[2].banned", is(false)))
                .andExpect(jsonPath("$[2].created_date", notNullValue()))
                .andExpect(jsonPath("$[2].updated_date", notNullValue()))
                .andExpect(jsonPath("$[3].id", notNullValue()))
                .andExpect(jsonPath("$[3].login", is("testUser4")))
                .andExpect(jsonPath("$[3].role", is(UserRole.TRADER_TEAM_LEADER.name())))
                .andExpect(jsonPath("$[3].banned", is(false)))
                .andExpect(jsonPath("$[3].created_date", notNullValue()))
                .andExpect(jsonPath("$[3].updated_date", notNullValue()))
                .andExpect(jsonPath("$[4].id", notNullValue()))
                .andExpect(jsonPath("$[4].login", is("testUser5")))
                .andExpect(jsonPath("$[4].role", is(UserRole.TRADER_TEAM.name())))
                .andExpect(jsonPath("$[4].banned", is(false)))
                .andExpect(jsonPath("$[4].created_date", notNullValue()))
                .andExpect(jsonPath("$[4].updated_date", notNullValue()));
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

