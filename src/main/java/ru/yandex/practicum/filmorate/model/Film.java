package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Date releaseDate;
    @Min(value = 1)
    private int duration;
    private int rate;
    private Mpa mpa;
    private List<Genre>  genres;
    private Set<Long> like;
}
