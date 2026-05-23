package com.booking.cinema.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String seatLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Users user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Define o valor padrão no Java também

    @Column( nullable = false)
    private LocalDateTime expiresAt;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Session session;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private BookingState state;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private TypeTicket ticket;

    public Booking() {}

    public Booking(Long id, String seatLabel, Users user, LocalDateTime createdAt, LocalDateTime expiresAt, BigDecimal price, BookingState state, Session session, TypeTicket ticket) {
        this.id = id;
        this.seatLabel = seatLabel;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.price = price;
        this.state = state;
        this.session = session;
        this.ticket = ticket;
    }

    public Booking(String seatLabel, Users user, LocalDateTime expiresAt, BigDecimal price, BookingState state, Session session, TypeTicket ticket) {
        this.seatLabel = seatLabel;
        this.user = user;
        this.expiresAt = expiresAt;
        this.price = price;
        this.state = state;
        this.session = session;
        this.ticket = ticket;
    }

    public Long getId() {
        return id;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public TypeTicket getTicket() {
        return ticket;
    }

    public void setTicket(TypeTicket ticket) {
        this.ticket = ticket;
    }
}
