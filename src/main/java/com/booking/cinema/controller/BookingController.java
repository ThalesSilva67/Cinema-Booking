package com.booking.cinema.controller;

import com.booking.cinema.doc.BookingControllerDoc;
import com.booking.cinema.dto.request.BookingRequestDTO;
import com.booking.cinema.dto.response.BookingResponseDTO;
import com.booking.cinema.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController implements BookingControllerDoc {
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> findById(@PathVariable Long id) {
        BookingResponseDTO response = bookingService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAll() {
        List<BookingResponseDTO> bookings = bookingService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookings);
    }
}
