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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CustomLoginSecurityTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void unauthenticatedUser_isRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginPage_isAccessible() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("IOTSTAR SHOP")))
            .andExpect(content().string(containsString("Username hoặc Email")));
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

        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Trang chủ UTEShop")))
            .andExpect(content().string(containsString("Nguyễn Hữu Trung")))
            .andExpect(content().string(containsString("(user01)")))
            .andExpect(content().string(containsString("user01@gmail.com")))
            .andExpect(content().string(containsString("ROLE_USER")));
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

        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Trang chủ UTEShop")))
            .andExpect(content().string(containsString("Nguyễn Hữu Trung")));
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
}
