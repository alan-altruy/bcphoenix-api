package com.bcphoenix.security;

import com.bcphoenix.controller.account.AccountController;
import com.bcphoenix.controller.admin.AdminUserController;
import com.bcphoenix.controller.visitor.LocationController;
import com.bcphoenix.service.LocationService;
import com.bcphoenix.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = {
        LocationController.class,
        AdminUserController.class,
        AccountController.class
})
@Import({SecurityConfig.class, SecurityErrorWriter.class})
class AccessRulesTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @MockitoBean
    private UserService userService;

    @Test
    void unVisiteurPeutLireLesLieux() throws Exception {
        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk());
    }

    @Test
    void unVisiteurNePeutPasOuvrirUneRouteAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @WithMockUser(roles = "USER")
    void unCompteSansRoleAdminEstRefuse() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unAdminPeutOuvrirUneRouteAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    void unVisiteurNePeutPasLireLesInfosDUnConnecte() throws Exception{
        mockMvc.perform(get("/api/account/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unAdminSansJetonNePeutPasCreerSurUneRouteAdmin() throws Exception{
        mockMvc.perform(post("/api/admin/users"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unPostSurUneRoutePubliqueEstRefuseMemePourUnAdmin() throws Exception{
        mockMvc.perform(post("/api/locations").with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

}
