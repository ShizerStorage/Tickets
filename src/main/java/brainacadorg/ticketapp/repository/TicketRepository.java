package brainacadorg.ticketapp.repository;


import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.model.TicketStatus;
import brainacadorg.ticketapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventIdAndStatus(TicketStatus status);

    List<Ticket> findByEventIdAndStatus(Long eventId, TicketStatus status);

    List<Ticket> findByUserEmail(String email);

    List<Ticket> findByUserEmailAndStatus(String email, TicketStatus status);

    List<Ticket> findByUser(User user);
}