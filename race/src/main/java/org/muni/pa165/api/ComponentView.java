package org.muni.pa165.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.muni.pa165.data.enums.ComponentType;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class ComponentView implements Serializable {

    private int weight;

    private int price;

    private String manufacturer;

    private String version;

    private ComponentType type;
}
