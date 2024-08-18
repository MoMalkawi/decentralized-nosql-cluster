package malkawi.logging.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.logging.web.controllers.services.AuthService;
import malkawi.logging.web.controllers.services.DatabaseService;
import malkawi.logging.web.controllers.services.RequestService;
import malkawi.logging.web.sessions.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor @SuppressWarnings("unused")
public class AuthController {

    private final AuthService authService;

    private final @Getter RequestService requestService;

    private final DatabaseService databaseService;

    @PostMapping("logout")
    public String deleteSession(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        authService.deleteSession(sessionId);
        return requestService.request("", sessionId, true);
    }

    @PostMapping("verify")
    public String verifyCredentials(HttpServletRequest request,
                              @RequestParam("username") String username, @RequestParam("password") String password) {
        String sessionId = request.getSession().getId();
        if(authService.authenticate(username, password, sessionId) != null)
            return requestService.request("", sessionId, true);
        return "badcredentials";
    }

    @PostMapping("request")
    public String requestCredentials(HttpServletRequest request, Model model) {
        UserSession session = authService.getOrCreateSession(null, request.getSession().getId());
        if(session != null) {
            model.addAttribute("username", session.getUser().getUsername());
            model.addAttribute("password", session.getUser().getPassword());
            return "new_credentials";
        }
        return "fail";
    }

}
