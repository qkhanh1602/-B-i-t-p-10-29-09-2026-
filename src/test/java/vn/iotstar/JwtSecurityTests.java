package vn.iotstar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.iotstar.models.LoginResponse;
import vn.iotstar.models.LoginUserModel;
import vn.iotstar.models.RegisterUserModel;
import vn.iotstar.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtSecurityTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc getMockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)
    void loginPage_isAccessible() throws Exception {
        getMockMvc().perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")));
    }

    @Test
    @Order(2)
    void profilePage_isAccessible() throws Exception {
        getMockMvc().perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Spring Boot REST API with AJAX Example")));
    }

    @Test
    @Order(3)
    void signup_createsUser() throws Exception {
        userRepository.findByEmail("newuser@hcmute.edu.vn").ifPresent(userRepository::delete);

        RegisterUserModel registerUser = new RegisterUserModel();
        registerUser.setEmail("newuser@hcmute.edu.vn");
        registerUser.setPassword("123456");
        registerUser.setFullName("Nguyen Van Test");

        getMockMvc().perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@hcmute.edu.vn"))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van Test"));
    }

    @Test
    @Order(4)
    void login_returnsValidJwtToken() throws Exception {
        LoginUserModel loginUser = new LoginUserModel();
        loginUser.setEmail("trungnh@hcmute.edu.vn");
        loginUser.setPassword("123456");

        MvcResult result = getMockMvc().perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.expiresIn").value(3600000))
                .andReturn();

        LoginResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        assertNotNull(response.getToken());

        // Test accessing /users/me with Bearer token
        getMockMvc().perform(get("/users/me")
                        .header("Authorization", "Bearer " + response.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("trungnh@hcmute.edu.vn"));
    }

    @Test
    @Order(5)
    void usersMe_withoutToken_fails() throws Exception {
        getMockMvc().perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    void usersAll_withToken_returnsUserList() throws Exception {
        LoginUserModel loginUser = new LoginUserModel();
        loginUser.setEmail("trungnh@hcmute.edu.vn");
        loginUser.setPassword("123456");

        MvcResult result = getMockMvc().perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

        getMockMvc().perform(get("/users/")
                        .header("Authorization", "Bearer " + response.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
