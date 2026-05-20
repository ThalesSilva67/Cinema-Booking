package com.booking.cinema.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "o nome da sala é obrigatório")
    @Column(length = 20, nullable = false)
    private String name;

    @Positive
    private int totalRows;

    @Positive
    private int seatPerRow;

    public Room() {}

    public Room(Long id, String name, int totalRows, int seatPerRow) {
        this.id = id;
        this.name = name;
        this.totalRows = totalRows;
        this.seatPerRow = seatPerRow;
    }

    public Room(String name, int totalRows, int seatPerRow) {
        this.name = name;
        this.totalRows = totalRows;
        this.seatPerRow = seatPerRow;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getSeatPerRow() {
        return seatPerRow;
    }

    public void setSeatPerRow(int seatPerRow) {
        this.seatPerRow = seatPerRow;
    }
}
