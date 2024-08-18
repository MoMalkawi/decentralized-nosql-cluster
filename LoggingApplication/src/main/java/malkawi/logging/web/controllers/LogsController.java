package malkawi.logging.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import malkawi.logging.database.entities.LogInfo;
import malkawi.logging.web.controllers.services.DatabaseService;
import malkawi.logging.web.controllers.services.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;

@Controller
@AllArgsConstructor
@RequestMapping("/logs")
public class LogsController {

    private final DatabaseService databaseService;

    private final RequestService requestService;

    @PostMapping("all")
    public String getAllLogs(HttpServletRequest request, Model model) {
        model.addAttribute("logs", databaseService.fetchAllLogs(request.getSession().getId()));
        return requestService.request("displaylogs", request.getSession().getId(), false);
    }

    @PostMapping("get")
    public String getLog(HttpServletRequest request, @RequestParam("log-id") int id, Model model) {
        LogInfo log = databaseService.fetchLog(id, request.getSession().getId());
        model.addAttribute("logs", log != null ? Collections.singletonList(log) : Collections.emptyList());
        return requestService.request("displaylogs", request.getSession().getId(), false);
    }

    @PostMapping("add-update")
    public String appendToLogCollection(HttpServletRequest request, @RequestParam("id") String id,
                                        @RequestParam("message") String message, @RequestParam("date") LocalDate date) {
        LogInfo info = new LogInfo(id.isEmpty() ? -1 : Integer.parseInt(id), message, date);
        return databaseService.appendLog(info, request.getSession().getId()) ?
                "success" : "fail";
    }

    @PostMapping("delete")
    public String deleteLog(HttpServletRequest request, @RequestParam("id") int id) {
        return databaseService.deleteLog(id, request.getSession().getId()) ? "success" : "fail";
    }

}
