package brainacadorg.ticketapp.controller;

import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.model.User;
import brainacadorg.ticketapp.service.TicketService;
import brainacadorg.ticketapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final TicketService ticketService;
    private final UserService userService;

    @GetMapping("/profile")
    public String getProfilePage(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        List<Ticket> tickets = ticketService.getUserTickets(email);

        model.addAttribute("user", user);
        model.addAttribute("tickets", tickets);

        return "profile";
    }
}
