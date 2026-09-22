package vn.iotstar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.repository.OtpTokenRepository;
import vn.iotstar.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CustomLoginSecurityTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void publicPages_areAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Dashboard")))
            .andExpect(content().string(containsString("Total Users")))
            .andExpect(content().string(containsString("Total Products")));

        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Đăng nhập")));

        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Đăng ký")));

        mockMvc.perform(get("/forgot-password"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Quên mật khẩu")));
    }

    @Test
    void unauthenticatedUser_accessingProducts_isRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginWithUsername_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "user01")
                .param("password", "123456")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        mockMvc.perform(get("/products").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Quản lý Products")));
    }

    @Test
    void loginWithEmail_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "user01@gmail.com")
                .param("password", "123456")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        mockMvc.perform(get("/products").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Quản lý Products")));
    }

    @Test
    void regularUser_accessingUsers_isForbidden() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "user01")
                .param("password", "123456")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        mockMvc.perform(get("/users").session(session))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminUser_accessingUsers_isAllowed() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "123456")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        mockMvc.perform(get("/users").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Quản lý Users")));
    }

    @Test
    void loginWithBadCredentials_fails() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "user01")
                .param("password", "wrongpassword")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    void logout_success() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?logout=true"));
    }

    @Test
    void registerFlow_createsOtpToken() throws Exception {
        String testEmail = "newbie" + System.currentTimeMillis() + "@gmail.com";
        String testUser = "newbie" + System.currentTimeMillis();

        mockMvc.perform(post("/register")
                .param("username", testUser)
                .param("email", testEmail)
                .param("fullName", "Newbie Test")
                .param("password", "123456")
                .param("confirmPassword", "123456")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/verify-otp?email=" + testEmail));

        OtpToken token = otpTokenRepository
            .findTopByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(testEmail, "REGISTER")
            .orElse(null);

        assertTrue(token != null, "OTP token should have been created in database");
    }
}
