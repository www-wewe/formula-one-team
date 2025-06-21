package org.muni.pa165.api;

import lombok.Data;
import org.muni.pa165.data.enums.ComponentType;

@Data
public class Component {

    private Long id;

    private int weight;

    private int price;

    private String manufacturer;

    private String version;

    private ComponentType type;
}
