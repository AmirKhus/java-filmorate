package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Date releaseDate;
    @Min(value = 1)
    private int duration;
}
