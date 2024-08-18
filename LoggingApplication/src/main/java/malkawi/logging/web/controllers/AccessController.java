package malkawi.logging.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import malkawi.logging.web.controllers.services.AuthService;
import malkawi.logging.web.controllers.services.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor @SuppressWarnings("unused")
public class AccessController {

    private final AuthService authService;

    private final @Getter RequestService requestService;

    @GetMapping("/")
    public String getIndex(HttpServletRequest request) {
        return requestService.request("index", request.getSession().getId(), false);
    }

    @GetMapping("login")
    public String getLogin(HttpServletRequest request) {
        return requestService.requestAuthPage(true, request.getSession().getId());
    }

    @GetMapping("register")
    public String getRegister(HttpServletRequest request) {
        return requestService.requestAuthPage(false, request.getSession().getId());
    }

}
