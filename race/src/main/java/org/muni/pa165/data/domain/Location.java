package org.muni.pa165.data.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {

    @Schema(description = "Country of the location",
            example = "Czech Republic")
    private String country;

    @Schema(description = "City of the location",
            example = "Brno")
    private String city;

    @Schema(description = "Street of the location",
            example = "Masaryk Circuit 123")
    private String street;
}
