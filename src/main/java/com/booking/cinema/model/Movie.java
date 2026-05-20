package com.booking.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "o titulo do filme é obrigatório")
    @Column(length = 100, nullable = false)
    private String title;

    @NotBlank(message = "a sinopse do filme é obrigatória")
    @Column(length = 255, nullable = false)
    private String description;

    @NotNull(message = "a duração do filme é obrigatória")
    @Column(nullable = false)
    private Integer duration;

    public Movie() {}

    public Movie(Long id, String title, String description, Integer duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public Movie(String title, String description, Integer duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
