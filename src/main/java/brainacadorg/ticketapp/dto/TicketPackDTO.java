package brainacadorg.ticketapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPackDTO {
    private double cost;
    private int count;
}