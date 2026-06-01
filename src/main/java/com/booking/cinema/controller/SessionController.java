package com.booking.cinema.controller;

import com.booking.cinema.doc.SessionControllerDoc;
import com.booking.cinema.dto.request.SessionRequestDTO;
import com.booking.cinema.dto.response.SessionResponseDTO;
import com.booking.cinema.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController implements SessionControllerDoc {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping()
    public ResponseEntity<SessionResponseDTO> create(@Valid @RequestBody SessionRequestDTO request) {
        SessionResponseDTO response = sessionService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> findById(@PathVariable Long id) {
        SessionResponseDTO response = sessionService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SessionRequestDTO request) {
        SessionResponseDTO updatedSession = sessionService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> getAll() {
        List<SessionResponseDTO> sessions = sessionService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sessions);
    }
}
