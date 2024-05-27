package com.flacko.trader.team.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.flacko.common.bank.Bank;
import com.flacko.common.country.Country;
import com.flacko.common.currency.Currency;
import com.flacko.common.exception.*;
import com.flacko.common.role.UserRole;
import com.flacko.common.state.PaymentState;
import com.flacko.merchant.service.MerchantService;
import com.flacko.payment.method.service.PaymentMethodService;
import com.flacko.payment.service.incoming.IncomingPaymentService;
import com.flacko.payment.service.outgoing.OutgoingPaymentService;
import com.flacko.terminal.service.TerminalService;
import com.flacko.trader.team.service.TraderTeam;

import com.flacko.trader.team.service.TraderTeamService;
import com.flacko.trader.team.service.exception.TraderTeamIllegalLeaderException;
import com.flacko.trader.team.service.exception.TraderTeamInvalidFeeRateException;
import com.flacko.trader.team.service.exception.TraderTeamMissingRequiredAttributeException;
import com.flacko.trader.team.webapp.rest.TraderTeamCreateRequest;
import com.flacko.user.service.User;
import com.flacko.user.service.UserService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
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
    private String traderTeamId1;
    private String paymentMethodId;
    private String traderTeamLeaderId1;
    private String traderTeamUserId1;
    private String traderTeamUserId2;

    private String traderTeamLeaderId2;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setup() throws Exception {
        class RandomStringGenerator {
            private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            public String randomAlphanumeric(int count) {
                StringBuilder builder = new StringBuilder();
                Random random = new Random();
                while (count-- != 0) {
                    int character = random.nextInt(ALPHANUMERIC_STRING.length());
                    builder.append(ALPHANUMERIC_STRING.charAt(character));
                }
                return builder.toString();
            }
        }

        RandomStringGenerator generator = new RandomStringGenerator();
        User merchantUser = userService.create()
                .withLogin(generator.randomAlphanumeric(10))
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

        traderTeamUserId1 = userService.create()
                .withLogin(generator.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();
        traderTeamUserId2 = userService.create()
                .withLogin(generator.randomAlphanumeric(10))
                .withPassword("qwerty654321")
                .withRole(UserRole.TRADER_TEAM)
                .build()
                .getId();
        traderTeamLeaderId1 = userService.create()
                .withLogin(generator.randomAlphanumeric(10))
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();
        traderTeamLeaderId2 = userService.create()
                .withLogin(generator.randomAlphanumeric(10))
                .withPassword("qwerty0000000")
                .withRole(UserRole.TRADER_TEAM_LEADER)
                .build()
                .getId();

        traderTeamId1 = traderTeamService.create()
                .withName("test_merchant")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withTraderIncomingFeeRate(BigDecimal.valueOf(0.018))
                .withTraderOutgoingFeeRate(BigDecimal.valueOf(0.018))
                .withLeaderIncomingFeeRate(BigDecimal.valueOf(0.002))
                .withLeaderOutgoingFeeRate(BigDecimal.valueOf(0.002))
                .build()
                .getId();



        String terminalId = terminalService.create()
                .withTraderTeamId(traderTeamId1)
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
                .withTraderTeamId(traderTeamId1)
                .withTerminalId(terminalId)
                .build()
                .getId();

        incomingPaymentId = incomingPaymentService.create()
                .withMerchantId(merchantId)
                .withTraderTeamId(traderTeamId1)
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
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        TraderTeam team2 = traderTeamService.create()
                .withName("TestTeam2")
                .withUserId(traderTeamUserId2)
                .withLeaderId(traderTeamLeaderId2)
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
                .andExpect((ResultMatcher) jsonPath("$[0].name",is(team1.getName())))
                .andExpect((ResultMatcher) jsonPath("$[0].userId",is(traderTeamUserId1)))
                .andExpect((ResultMatcher) jsonPath("$[0].leaderId",is(traderTeamLeaderId1)))
                .andExpect(jsonPath("$[1].id",notNullValue()))
                .andExpect((ResultMatcher) jsonPath("$[1].name",is(team2.getName())))
                .andExpect((ResultMatcher) jsonPath("$[1].userId",is(traderTeamUserId2)))
                .andExpect((ResultMatcher) jsonPath("$[1].leaderId",is(traderTeamLeaderId2)));
        verify(traderTeamService, times(1)).list();
    }
    @Test
    public void testGetTraderTeamById() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
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
                .andExpect((ResultMatcher) jsonPath("$.name", is(team1.getName())))
                .andExpect((ResultMatcher) jsonPath("$.userId", is(traderTeamUserId1)))
                .andExpect((ResultMatcher) jsonPath("$.leaderId", is(traderTeamLeaderId2)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.updatedDate", notNullValue()));

    }

    @Test
    public void testGetNonexistentTraderTeamById() throws Exception {
        // Выполнение GET-запроса на несуществующий ID команды
        mockMvc.perform(get("/trader-teams/nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamNotFoundException))
                .andExpect(result -> assertEquals("Trader team nonexistent-id not found", result.getResolvedException().getMessage()));
    }
    @Test
    public void testCreateTraderTeam() throws Exception {
        // Создание запроса для создания торговой команды
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                "TestTeam", // name
                "TestUser", // userId
                Country.RUSSIA, // country
                "TestLeader", // leaderId
                new BigDecimal("0.01"), // traderIncomingFeeRate
                new BigDecimal("0.01"), // traderOutgoingFeeRate
                new BigDecimal("0.01"), // leaderIncomingFeeRate
                new BigDecimal("0.01")  // leaderOutgoingFeeRate
        );

        // Выполнение POST-запроса к эндпоинту
        MvcResult result = (MvcResult) mockMvc.perform(post("/trader-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", is("TestTeam")))
                .andExpect((ResultMatcher) jsonPath("$.userId", is("TestUser")))
                .andExpect((ResultMatcher) jsonPath("$.country", is("RUSSIA")))
                .andExpect((ResultMatcher) jsonPath("$.leaderId", is("TestLeader")))
                .andExpect((ResultMatcher) jsonPath("$.traderIncomingFeeRate", is(0.01)))
                .andExpect((ResultMatcher) jsonPath("$.traderOutgoingFeeRate", is(0.01)))
                .andExpect((ResultMatcher) jsonPath("$.leaderIncomingFeeRate", is(0.01)))
                .andExpect((ResultMatcher) jsonPath("$.leaderOutgoingFeeRate", is(0.01)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.updatedDate", notNullValue()));

        // Дополнительная Проверка ответа
        String content = result.getResponse().getContentAsString();
        TraderTeam responseTeam = new ObjectMapper().readValue(content, TraderTeam.class);

        assertNotNull(responseTeam.getId());
        assertFalse(responseTeam.isVerified());
        assertFalse(responseTeam.isIncomingOnline());
        assertFalse(responseTeam.isOutgoingOnline());
        assertFalse(responseTeam.isKickedOut());
    }


    @Test
    public void testCreateTraderTeam_missingAttribute() throws Exception {
        // Создание запроса для создания торговой команды без обязательного атрибута
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                null, // name - обязательный атрибут, установленный в null
                "TestUser", // userId
                Country.RUSSIA, // country
                "TestLeader", // leaderId
                new BigDecimal("0.01"), // traderIncomingFeeRate
                new BigDecimal("0.01"), // traderOutgoingFeeRate
                new BigDecimal("0.01"), // leaderIncomingFeeRate
                new BigDecimal("0.01")  // leaderOutgoingFeeRate
        );

        // Выполнение POST-запроса к эндпоинту
        mockMvc.perform(post("/trader-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamMissingRequiredAttributeException))
                .andExpect(result -> assertEquals("Missing required name attribute for trader team unknown", result.getResolvedException().getMessage()));
    }
    @Test
    public void testCreateTraderTeam_UserNotFoundException() throws Exception {
        // Создание запроса для создания торговой команды с несуществующим пользователем
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                "TestTeam", // name
                "NonexistentUser", // userId
                Country.RUSSIA, // country
                "TestLeader", // leaderId
                new BigDecimal("0.01"), // traderIncomingFeeRate
                new BigDecimal("0.01"), // traderOutgoingFeeRate
                new BigDecimal("0.01"), // leaderIncomingFeeRate
                new BigDecimal("0.01")  // leaderOutgoingFeeRate
        );

        // Выполнение POST-запроса к эндпоинту
        mockMvc.perform(post("/trader-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User NonexistentUser not found", result.getResolvedException().getMessage()));
    }
    @Test
    public void testCreateTraderTeam_IllegalLeaderException() throws Exception {
        // Создание запроса для создания торговой команды
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                "TestTeam", // name
                "TestUser", // userId
                Country.RUSSIA, // country
                "IllegalLeader", // leaderId
                new BigDecimal("0.01"), // traderIncomingFeeRate
                new BigDecimal("0.01"), // traderOutgoingFeeRate
                new BigDecimal("0.01"), // leaderIncomingFeeRate
                new BigDecimal("0.01")  // leaderOutgoingFeeRate
        );

        // Выполнение POST-запроса к эндпоинту и ожидание исключения
        Exception exception = assertThrows(TraderTeamIllegalLeaderException.class, () -> {
            mockMvc.perform(post("/trader-teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));});
        String expectedMessage = "User TestUser cannot be chosen as leader of trader team TestTeam";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateTraderTeamWithTraderTeamInvalidFeeRate() throws Exception {
        // Создание запроса для создания торговой команды с недопустимой ставкой
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                "TestTeam",
                "TestUser",
                Country.RUSSIA,
                "TestLeader",
                new BigDecimal("-0.01"), // traderIncomingFeeRate
                new BigDecimal("-0.01"), // traderOutgoingFeeRate
                new BigDecimal("-0.01"), // leaderIncomingFeeRate
                new BigDecimal("-0.01")  // leaderOutgoingFeeRate
        );

        // Выполнение POST-запроса к эндпоинту
        mockMvc.perform(post("/trader-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamInvalidFeeRateException))
                .andExpect(result -> assertEquals("Fee rate cannot be less than 0 for trader team TestTeam", result.getResolvedException().getMessage()));
    }

    @Test
    public void testUnauthorizedAccessToCreateTraderTeam() throws Exception {
        // Создание запроса для создания торговой команды
        TraderTeamCreateRequest request = new TraderTeamCreateRequest(
                "TestTeam",
                "TestUser",
                Country.RUSSIA,
                "TestLeader",
                new BigDecimal("0.01"),
                new BigDecimal("0.01"),
                new BigDecimal("0.01"),
                new BigDecimal("0.01")
        );

        // Выполнение POST-запроса без авторизации и ожидание UnauthorizedAccessException
        mockMvc.perform(post("/trader-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedAccessException));
    }

    @Test
    public void testDeleteTraderTeam() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        // Выполнение DELETE-запроса к эндпоинту
        mockMvc.perform(delete("/trader-teams/" + team1.getId()))
                .andExpect(status().isOk());

        // Проверка, что команда была удалена
        mockMvc.perform(get("/trader-teams/" + team1.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.deletedDate", notNullValue()));
    }

    @Test
    public void testDeleteTraderTeam_NonexistentId() throws Exception {
        // Выполнение DELETE-запроса на несуществующий ID команды
        mockMvc.perform(delete("/trader-teams/nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamNotFoundException))
                .andExpect(result -> assertEquals(String.format("Trader team not found by %s %s", "id", "nonexistent-id"), result.getResolvedException().getMessage()));
    }

    @Test
    public void testTraderTeamSetIncomingOnline() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        // Выполнение PATCH-запроса к эндпоинту
        mockMvc.perform(patch("/trader-teams/" + team1.getId() + "/incoming-online"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team1.getId())))
                .andExpect((ResultMatcher) jsonPath("$.name", is(team1.getName())))
                .andExpect((ResultMatcher) jsonPath("$.userId", is(traderTeamUserId1)))
                .andExpect((ResultMatcher) jsonPath("$.leaderId", is(traderTeamLeaderId1)))
                .andExpect((ResultMatcher) jsonPath("$.verified", is(team1.isVerified()))) // Проверка поля "verified"
                .andExpect((ResultMatcher) jsonPath("$.incomingOnline", is(true)))
                .andExpect((ResultMatcher) jsonPath("$.outgoingOnline", is(team1.isOutgoingOnline()))) // Проверка поля "outgoingOnline"
                .andExpect((ResultMatcher) jsonPath("$.createdDate", is(team1.getCreatedDate()))) // Проверка даты создания
                .andExpect((ResultMatcher) jsonPath("$.updatedDate", is(team1.getUpdatedDate()))); // Проверка даты обновления
    }

    @Test
    public void testTraderTeamSetIncomingOnline_withNonexistentTraderTeamId() throws Exception {
        // Выполнение PATCH-запроса к эндпоинту с несуществующим ID команды
        mockMvc.perform(patch("/trader-teams/nonexistent-id/incoming-online"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TraderTeamNotFoundException))
                .andExpect(result -> assertEquals(String.format("Trader team not found by %s %s", "id", "nonexistent-id"), result.getResolvedException().getMessage()));
    }
    @Test
    public void testTraderTeamSetIncomingOffline() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .withIncomingOnline(true) // установка в true перед выполнением теста
                .build();

        // Выполнение PATCH-запроса к эндпоинту и проверка ответа
        mockMvc.perform(patch("/trader-teams/" + team1.getId() + "/incoming-offline"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team1.getId())))
                .andExpect((ResultMatcher) jsonPath("$.incomingOnline", is(false)));

        // Дополнительная проверка: убедиться, что в базе данных торговая команда обновлена
        TraderTeam updatedTeam = traderTeamService.get(team1.getId());
        assertFalse(updatedTeam.isIncomingOnline());
    }
    @Test
    public void testTraderTeamSetIncomingOffline_withNonexistentTraderTeamId() {
        // Проверка исключения TraderTeamNotFoundException
        Exception exception = assertThrows(TraderTeamNotFoundException.class, () -> traderTeamService.get("invalid-id"));
        assertTrue(exception.getMessage().contains("Trader team invalid-id not found"));
    }
    @Test
    public void testTraderTeamSetOutgoingOnlineStatus() throws Exception {
        // Создание запроса с помощью TraderTeamService
        TraderTeam team = traderTeamService.create()
                .withName("TestTeam")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        // Выполнение PATCH-запроса к эндпоинту
        mockMvc.perform(patch("/trader-teams/{id}/outgoing-online", team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team.getId())))
                .andExpect((ResultMatcher) jsonPath("$.outgoingOnline", is(true)));

        // Проверка, что статус был фактически обновлен
        TraderTeam updatedTeam = traderTeamService.get(team.getId());
        assertTrue(updatedTeam.isOutgoingOnline());
    }
    @Test
    public void testTraderTeamIncomingOffline_withNonexistentTraderTeamId() {
        // Попытка обновления несуществующей команды
        assertThrows(TraderTeamNotFoundException.class, () -> {
            // Выполнение PATCH-запроса к эндпоинту для несуществующей команды
            mockMvc.perform(patch("/trader-teams/" + "nonExistingTeamId" + "/incoming-offline"))
                    .andExpect(status().isNotFound());
        });
    }
    @Test
    public void testTraderTeamOutgoingOfflineStatus() throws Exception {
        // Создание запроса с помощью TraderTeamService
        TraderTeam team = traderTeamService.create()
                .withName("TestTeam")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .withOutgoingOnline(true) // установка в true перед выполнением теста
                .build();

        // Выполнение PATCH-запроса к эндпоинту
        mockMvc.perform(patch("/trader-teams/{id}/outgoing-offline", team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("false"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team.getId())))
                .andExpect((ResultMatcher) jsonPath("$.outgoingOnline", is(false)));

        // Проверка, что статус был фактически обновлен
        TraderTeam updatedTeam = traderTeamService.get(team.getId());
        assertFalse(updatedTeam.isOutgoingOnline());
    }
    @Test
    public void testTraderTeamOutgoingOffline_withNonexistentTraderTeamId() {
        // Попытка обновления несуществующей команды
        assertThrows(TraderTeamNotFoundException.class, () -> {
            // Выполнение PATCH-запроса к эндпоинту для несуществующей команды
            mockMvc.perform(patch("/trader-teams/" + "nonExistingTeamId" + "/outgoing-offline"))
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    public void testTraderTeamKickOut() throws Exception {
        // Создание торговой команды с помощью TraderTeamService
        TraderTeam team = traderTeamService.create()
                .withName("TestTeam")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .build();

        // Выполнение PATCH-запроса к эндпоинту
        MvcResult result = mockMvc.perform(patch("/trader-teams/kick-out/" + team.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Проверка ответа
        String content = result.getResponse().getContentAsString();
        TraderTeam responseTeam = new ObjectMapper().readValue(content, TraderTeam.class);

        assertTrue(responseTeam.isKickedOut());
    }
    @Test
    public void testTraderTeamKickOut_withNonexistentTraderTeam() throws Exception {
        // Попытка выгнать несуществующую команду
        assertThrows(TraderTeamNotFoundException.class, () -> {
            // Выполнение PATCH-запроса к эндпоинту для несуществующей команды
            mockMvc.perform(patch("/trader-teams/kick-out/" + "nonExistingTeamId")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    public void testTraderTeamGetBack() throws Exception {
        // Создание тестовой торговой команды с помощью TraderTeamService
        TraderTeam team1 = traderTeamService.create()
                .withName("TestTeam1")
                .withUserId(traderTeamUserId1)
                .withLeaderId(traderTeamLeaderId1)
                .withCountry(Country.RUSSIA)
                .withTraderIncomingFeeRate(new BigDecimal("0.01"))
                .withTraderOutgoingFeeRate(new BigDecimal("0.01"))
                .withLeaderIncomingFeeRate(new BigDecimal("0.01"))
                .withLeaderOutgoingFeeRate(new BigDecimal("0.01"))
                .withKickedOut(true) // установка в true перед выполнением PATCH-запроса
                .build();

        // Выполнение PATCH-запроса к эндпоинту
        mockMvc.perform(patch("/trader-teams/get-back/" + team1.getId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(team1.getId())))
                .andExpect((ResultMatcher) jsonPath("$.name", is(team1.getName())))
                .andExpect((ResultMatcher) jsonPath("$.userId", is(traderTeamUserId1)))
                .andExpect((ResultMatcher) jsonPath("$.leaderId", is(traderTeamLeaderId1)))
                .andExpect((ResultMatcher) jsonPath("$.kickedOut", is(false))) // проверка, что статус 'kickedOut' установлен в false
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.updatedDate", notNullValue()));
    }
    @Test
    public void testTraderTeamGetBack_withNonexistentTraderTeamId() {
        // Attempt to update a nonexistent team
        assertThrows(TraderTeamNotFoundException.class, () -> {
            // Perform PATCH request to the endpoint for a nonexistent team
            mockMvc.perform(patch("/trader-teams/get-back/" + "nonExistingTeamId"))
                    .andExpect(status().isNotFound());
        });
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

