package brainacadorg.ticketapp.service;

import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.model.TicketStatus;
import brainacadorg.ticketapp.model.User;
import brainacadorg.ticketapp.repository.TicketRepository;
import brainacadorg.ticketapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PdfTicketService pdfTicketService;

    public List<Ticket> findAvailableTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.FREE);
    }

    public void purchaseTicket(Long ticketId, String userEmail) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ticket.setStatus(TicketStatus.SOLD);
        ticket.setUser(user);
        ticketRepository.save(ticket);
    }

    public List<Ticket> findUserTickets(String userEmail) {
        return ticketRepository.findByUserEmail(userEmail);
    }

    public List<Ticket> getAvailableTickets(Long eventId) {
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.FREE);
    }

    public List<Ticket> getAvailableTickets() {
        return ticketRepository.findByEventIdAndStatus(TicketStatus.FREE);
    }

    @Transactional
    public void buyTicket(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new IllegalStateException("Квиток вже продано!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Користувач не знайдений"));

        ticket.setStatus(TicketStatus.SOLD);
        ticket.setUser(user);
        ticketRepository.save(ticket);
    }

    public List<Ticket> getUserTickets(String email) {
        return ticketRepository.findByUserEmail(email);
    }

    public List<Ticket> getUserPurchaseHistory(String email) {
        return ticketRepository.findByUserEmailAndStatus(email, TicketStatus.SOLD);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow();
    }

    public void assignTicketToUser(Long ticketId, Long UserId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Квиток не знайдено"));

        User user = userRepository.findById(UserId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new RuntimeException("Квиток вже проданий");
        }

        ticket.setUser(user);
        ticket.setStatus(TicketStatus.SOLD);
        ticketRepository.save(ticket);

        byte[] pdf = pdfTicketService.generateTicketPdf(ticket);

        emailService.sendTicketEmail(user.getEmail(), pdf, "ticket_" + ticketId + ".pdf");
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getAvailableTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.FREE);
    }

    @Transactional
    public void assignTicketToUser(Long ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Квиток не знайдено"));

        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new RuntimeException("Квиток вже продано");
        }

        ticket.setUser(user);
        ticket.setStatus(TicketStatus.SOLD);
        ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByCustomer(User user) {
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }
}