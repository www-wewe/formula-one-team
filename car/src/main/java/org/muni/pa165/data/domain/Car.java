package org.muni.pa165.data.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private Long mainDriver;

    @NotNull
    private String carMake;

    @Nullable
    @ElementCollection(targetClass = Long.class,
            fetch = FetchType.EAGER)
    private Set<Long> testDrivers;

    @NotNull
    @ElementCollection(targetClass = Long.class,
            fetch = FetchType.EAGER)
    private Set<Long> components;
}
