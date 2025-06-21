package org.muni.pa165.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.muni.pa165.data.domain.DriverPerk;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverCreateViewDto {

    @Schema(description = "name of the driver",
            example = "John")
    private String name;

    @Schema(description = "surname of the driver",
            example = "Doe")
    private String surname;

    @Schema(description = "country of origin",
            example = "Netherlands")
    private String nationality;

    @Schema(description = "perk of the driver",
            example = "HAMMER_TIME")
    private DriverPerk perk;
}
