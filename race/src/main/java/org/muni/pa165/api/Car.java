package org.muni.pa165.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car implements Serializable {

    private Long id;

    private Long mainDriver;

    private String carMake;

    private Set<Long> testDrivers;

    private Set<Long> components;
}
