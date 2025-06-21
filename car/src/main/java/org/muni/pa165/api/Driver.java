package org.muni.pa165.api;

import lombok.Data;
import org.muni.pa165.data.enums.DriverPerk;

@Data
public class Driver {

    private Long id;

    private String name;

    private String surname;

    private String nationality;

    private DriverPerk perk;
}
