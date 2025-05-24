package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.model.Event;
import brainacadorg.ticketapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final EventService eventService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/auth/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/events")
    public String eventsPage(Model model) {
        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "events";
    }
}
