package org.muni.pa165.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarCreateViewDto {

    @Schema(example = "0")
    private Long mainDriver;

    @Schema(example = "Ferrari")
    private String carMake;

    private Set<Long> testDrivers;

    private Set<Long> components;
}
