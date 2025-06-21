package org.muni.pa165.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.muni.pa165.data.enums.ComponentType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentViewDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "50")
    private int weight;

    @Schema(example = "100")
    private int price;

    @Schema(example = "Mercedes")
    private String manufacturer;

    @Schema(example = "V8")
    private String version;

    @Schema(example = "ENGINE")
    private ComponentType type;
}
