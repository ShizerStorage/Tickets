package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.model.Event;
import brainacadorg.ticketapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminController {
    private final EventService eventService;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin/events";
    }

    @GetMapping("/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/event_form";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event) {
        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/edit/{id}")
    public String editEventForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "admin/event_form";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, @ModelAttribute Event event) {
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/admin/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }
}