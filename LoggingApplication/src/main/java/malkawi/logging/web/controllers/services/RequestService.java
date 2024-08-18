package malkawi.logging.web.controllers.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestService {

    private final AuthService authService;

    public String request(String pageName, String sessionId, boolean redirect) {
        if(authService.isAuthenticated(sessionId))
            return redirect ? "redirect:/" + pageName : pageName;
        return "redirect:/" + loginPage();
    }

    public String requestAuthPage(boolean login, String sessionId) {
        boolean authenticated = authService.isAuthenticated(sessionId);
        if(authenticated)
            return redirect("index");
        return login ? "login" : "register";
    }

    public String redirect(String pageName) {
        return "redirect:/" + pageName;
    }

    public String loginPage() {
        return "login";
    }

}
