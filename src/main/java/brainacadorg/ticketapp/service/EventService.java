package brainacadorg.ticketapp.service;

import brainacadorg.ticketapp.dto.EventCreationDTO;
import brainacadorg.ticketapp.dto.TicketPackDTO;
import brainacadorg.ticketapp.model.*;
import brainacadorg.ticketapp.repository.EventRepository;
import brainacadorg.ticketapp.repository.PlaceRepository;
import brainacadorg.ticketapp.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final TicketRepository ticketRepository;

    public Event createEvent(EventCreationDTO dto) {
        Place place = placeRepository.findByName(dto.getPlace().getName())
                .orElseGet(() -> placeRepository.save(
                        Place.builder()
                                .name(dto.getPlace().getName())
                                .address(dto.getPlace().getAddress())
                                .build()
                ));

        Event event = Event.builder()
                .name(dto.getName())
                .eventDate(dto.getEventDate())
                .place(place)
                .build();
        eventRepository.save(event);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketPackDTO ticketPack : dto.getTicketPacks()) {
            for (int i = 0; i < ticketPack.getCount(); i++) {
                tickets.add(Ticket.builder()
                        .cost(ticketPack.getCost())
                        .status(TicketStatus.FREE)
                        .event(event)
                        .number(i + 1)
                        .build());
            }
        }
        ticketRepository.saveAll(tickets);
        event.setTickets(tickets);

        return event;
    }

    public List<Ticket> findAvailableTickets(String eventName) {
        return ticketRepository.findByUserEmailAndStatus(eventName, TicketStatus.FREE);
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository.findUpcomingEvents();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDate.now());
    }

    public List<Event> findEventsByName(String query) {
        return eventRepository.findEventsByName(query);
    }
}