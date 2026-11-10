package com.bcphoenix.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints d'authentification (Login / Logout)")
public class OpenApiAuthController {

    @Operation(
        summary = "Connexion utilisateur",
        description = "Authentifie l'utilisateur via des champs de formulaire (application/x-www-form-urlencoded)."
    )
    @ApiResponse(responseCode = "204", description = "Connexion réussie (Cookie de session créé)")
    @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void login(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "password") String password
    ) {
        // Géré en interne par Spring Security (formLogin)
        throw new UnsupportedOperationException("Géré par Spring Security");
    }

    @Operation(
        summary = "Déconnexion utilisateur",
        description = "Invalide la session en cours et supprime le cookie d'authentification."
    )
    @ApiResponse(responseCode = "204", description = "Déconnexion réussie")
    @PostMapping("/logout")
    public void logout() {
        // Géré par Spring Security
        throw new UnsupportedOperationException("Géré par Spring Security");
    }
}
