package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exceptions.annotation.AfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    private String name;
    @Size(message = "Description is too long", max = 200)
    @NotBlank(message = "Description can not be empty")
    @NotNull(message = "Description can not be null")
    private String description;

    @NotNull(message = "Release Date can not be null")
    @AfterCinemaBirthday(message = "The film appeared before cinematography")
    private LocalDate releaseDate;
    @NotNull(message = "Duration can not be null")
    @Positive(message = "Duration is negative or zero")
    private int duration;
    private int rate;
    @NotNull(message = "MPA rating can not be null")
    private Mpa mpa;
    private List<Genre>  genres;
    private Set<Long> like;
}
