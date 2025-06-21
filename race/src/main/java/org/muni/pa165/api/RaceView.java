package org.muni.pa165.api;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.muni.pa165.data.domain.Location;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class RaceView implements Serializable {

    private String name;

    private Location location;

    private LocalDate date;

    @Nullable
    private CarView car1;

    @Nullable
    private CarView car2;
}
