package brainacadorg.ticketapp;

import brainacadorg.ticketapp.model.User;
import brainacadorg.ticketapp.model.Ticket;
import brainacadorg.ticketapp.model.Event;
import brainacadorg.ticketapp.model.TicketStatus;
import brainacadorg.ticketapp.repository.TicketRepository;
import brainacadorg.ticketapp.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("ivanov@gmail.com");

        event = new Event();
        event.setId(1L);
        event.setName("Концерт");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCost(100.0);
        ticket.setNumber(10);
        ticket.setStatus(TicketStatus.FREE);
        ticket.setEvent(event);
    }

    @Test
    void testGetAvailableTicketsByEvent() {
        when(ticketRepository.findByEventIdAndStatus(event.getId(), TicketStatus.FREE))
                .thenReturn(List.of(ticket));

        List<Ticket> availableTickets = ticketService.getAvailableTicketsByEvent(event.getId());

        assertFalse(availableTickets.isEmpty());
        assertEquals(1, availableTickets.size());
        assertEquals(ticket.getId(), availableTickets.getFirst().getId());

        verify(ticketRepository, times(1)).findByEventIdAndStatus(event.getId(), TicketStatus.FREE);
    }

    @Test
    void testAssignTicketToUser_Success() {
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        ticketService.assignTicketToUser(ticket.getId(), user);

        assertEquals(TicketStatus.SOLD, ticket.getStatus());
        assertEquals(user, ticket.getUser());

        verify(ticketRepository, times(1)).findById(ticket.getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testAssignTicketToUser_TicketAlreadySold() {
        ticket.setStatus(TicketStatus.SOLD);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        Exception exception = assertThrows(RuntimeException.class, () ->
                ticketService.assignTicketToUser(ticket.getId(), user)
        );

        assertEquals("Квиток вже продано", exception.getMessage());

        verify(ticketRepository, times(1)).findById(ticket.getId());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void testGetTicketsByUser() {
        when(ticketRepository.findByUser(user)).thenReturn(List.of(ticket));

        List<Ticket> UserTickets = ticketService.getTicketsByUser(user);

        assertFalse(UserTickets.isEmpty());
        assertEquals(1, UserTickets.size());
        assertEquals(ticket.getId(), UserTickets.getFirst().getId());

        verify(ticketRepository, times(1)).findByUser(user);
    }
}
