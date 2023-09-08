package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1)
    private int duration;
    private Set<Long> like;
    private long genreId;
    private long МРАId;
}
