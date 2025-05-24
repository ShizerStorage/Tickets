package brainacadorg.ticketapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {
    private String name;
    private String address;
}