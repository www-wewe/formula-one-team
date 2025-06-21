package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring MVC Controller.
 * Handles HTTP requests by preparing data in model and passing it to Thymeleaf HTML templates.
 */
@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private String token;

    /**
     * Home page accessible even to non-authenticated users. Displays user personal data.
     */
    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser user) {
        log.debug("********************************************************");
        log.debug("* index() called                                       *");
        log.debug("********************************************************");
        log.debug("user {}", user == null ? "is anonymous" : user.getSubject());

        // put obtained user data into a model attribute named "user"
        model.addAttribute("user", user);

        // put issuer name into a model attribute named "issuerName"
        if (user != null) {
            model.addAttribute("issuerName",
                    "https://oidc.muni.cz/oidc/".equals(user.getIssuer().toString()) ? "MUNI" : "Google");
            return "redirect:/details";
        }

        // return the name of a Thymeleaf HTML template that
        // will be searched in src/main/resources/templates with .html suffix
        return "index";
    }

    @GetMapping("/details")
    public String details(Model model, @AuthenticationPrincipal OidcUser user,
                          @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oauth2Client) {

        model.addAttribute("user", user);
        token = oauth2Client.getAccessToken().getTokenValue();
        model.addAttribute("token", token);
        model.addAttribute("issuerName",
                "https://oidc.muni.cz/oidc/".equals(user.getIssuer().toString()) ? "MUNI" : "Google");
        return "details";
    }

    @GetMapping("/token")
    public ResponseEntity<String> token() {
        if (token == null) {
            return ResponseEntity
                    .status(404)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Not logged in.");
        }

        return ResponseEntity
                .status(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body(token);
    }
}