package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.dto.EventCreationDTO;
import brainacadorg.ticketapp.model.Event;
import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/")
    public String getEvents(@RequestParam(required = false) String query, Model model) {
        List<Event> events;

        if (query != null && !query.isEmpty()) {
            events = eventService.findEventsByName(query);
        } else {
            events = eventService.getAllEvents();
        }

        model.addAttribute("events", events);
        return "events";
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody EventCreationDTO eventDTO) {
        Event event = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/available-tickets")
    public ResponseEntity<List<Ticket>> getAvailableTickets(@RequestParam String eventName) {
        List<Ticket> tickets = eventService.findAvailableTickets(eventName);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventService.findUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events")
    public String getEvents(@RequestParam(required = false) String query,
                            @RequestParam(required = false) boolean upcoming, Model model) {
        List<Event> events;

        if (query != null && !query.isEmpty()) {
            events = eventService.findEventsByName(query);
        } else if (upcoming) {
            events = eventService.getUpcomingEvents();
        } else {
            events = eventService.getAllEvents();
        }

        model.addAttribute("events", events);
        return "events";
    }
}