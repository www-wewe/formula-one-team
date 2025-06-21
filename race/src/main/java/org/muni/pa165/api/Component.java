package org.muni.pa165.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.muni.pa165.data.enums.ComponentType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Component {

    private Long id;

    private int weight;

    private int price;

    private String manufacturer;

    private String version;

    private ComponentType type;
}
