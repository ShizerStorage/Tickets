package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final TicketService ticketService;

    @GetMapping("/tickets")
    public String myTickets(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Ticket> tickets = ticketService.getUserTickets(userDetails.getUsername());
        model.addAttribute("tickets", tickets);
        return "my-tickets";
    }

    @GetMapping("/purchase-history")
    public String purchaseHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Ticket> tickets = ticketService.getUserPurchaseHistory(userDetails.getUsername());
        model.addAttribute("tickets", tickets);
        return "purchase-history";
    }

}