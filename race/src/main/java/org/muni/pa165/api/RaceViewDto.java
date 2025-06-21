package org.muni.pa165.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.muni.pa165.data.domain.Location;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaceViewDto {

    @Schema(description = "ID of the race",
            example = "1")
    private Long id;

    @Schema(description = "Name of the race",
            example = "Grand Prix")
    private String name;

    @Schema(description = "Location of the race")
    private Location location;

    @Schema(description = "Date of the race",
            example = "2022-12-24")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Schema(description = "ID of the first car",
            example = "1")
    private Long car1Id;

    @Schema(description = "ID of the second car",
            example = "2")
    private Long car2Id;
}
