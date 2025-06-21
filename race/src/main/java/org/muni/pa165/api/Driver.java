package org.muni.pa165.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.muni.pa165.data.enums.DriverPerk;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver implements Serializable {

    private Long id;

    private String name;

    private String surname;

    private String nationality;

    private DriverPerk perk;
}

