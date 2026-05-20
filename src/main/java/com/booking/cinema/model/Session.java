package com.booking.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Room room;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    public Session() {}

    public Session(Long id, Movie movie, Room room, LocalDateTime startTime, BigDecimal price) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.price = price;
    }

    public Session(Movie movie, Room room, LocalDateTime startTime, BigDecimal price) {
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Movie getMovieId() {
        return movie;
    }

    public void setMovieId(Movie movie) {
        this.movie = movie;
    }

    public Room getRoomId() {
        return room;
    }

    public void setRoomId(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
