package brainacadorg.ticketapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreationDTO {
    private String name;
    private LocalDate eventDate;
    private List<TicketPackDTO> ticketPacks;
    private PlaceDTO place;
}