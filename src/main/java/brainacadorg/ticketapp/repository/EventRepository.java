package brainacadorg.ticketapp.repository;

import brainacadorg.ticketapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.eventDate >= CURRENT_DATE ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents();
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByEventDateAfter(LocalDate date);
    List<Event> findEventsByName(String query);
}