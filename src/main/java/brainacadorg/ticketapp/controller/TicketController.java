package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.model.User;
import brainacadorg.ticketapp.service.EventService;
import brainacadorg.ticketapp.service.PdfTicketService;
import brainacadorg.ticketapp.service.TicketService;
import brainacadorg.ticketapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final EventService eventService;
    private final PdfTicketService pdfTicketService;
    private final UserService userService;

    @GetMapping("/")
    public String getTickets(@RequestParam(required = false) String status, Model model) {
        List<Ticket> tickets;

        if ("FREE".equalsIgnoreCase(status)) {
            tickets = ticketService.getAvailableTickets();
        } else {
            tickets = ticketService.getAllTickets();
        }

        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/{eventId}")
    public String getTickets(@PathVariable Long eventId, Model model) {
        model.addAttribute("event", eventService.getEventById(eventId));
        List<Ticket> tickets = ticketService.getAvailableTickets(eventId);
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @PostMapping("/purchase/{ticketId}")
    public String purchaseTicket(@PathVariable Long ticketId, @AuthenticationPrincipal UserDetails userDetails) {
        ticketService.purchaseTicket(ticketId, userDetails.getUsername());
        return "redirect:/profile";
    }

    @GetMapping("/download/{ticketId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);

        byte[] pdf = pdfTicketService.generateTicketPdf(ticket);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("ticket_" + ticket.getId() + ".pdf")
                .build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/available")
    public String getAvailableTickets(@RequestParam Long eventId, Model model) {
        List<Ticket> tickets = ticketService.getAvailableTicketsByEvent(eventId);
        model.addAttribute("tickets", tickets);
        return "available-tickets";
    }

    @PostMapping("/buy/{ticketId}")
    public String buyTicket(@PathVariable Long ticketId, @AuthenticationPrincipal UserDetails userDetails) {
        User User = UserService.getUserByEmail(userDetails.getUsername());
        ticketService.assignTicketToUser(ticketId, User);
        return "redirect:/my-tickets";
    }

    @GetMapping("/my-tickets")
    public String getMyTickets(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User User = UserService.getUserByEmail(userDetails.getUsername());
        List<Ticket> tickets = ticketService.getTicketsByUser(User);
        model.addAttribute("tickets", tickets);
        return "my-tickets";
    }
}