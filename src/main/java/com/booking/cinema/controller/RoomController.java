package com.booking.cinema.controller;

import com.booking.cinema.dto.request.RoomRequestDTO;
import com.booking.cinema.dto.response.RoomResponseDTO;
import com.booking.cinema.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping()
    public ResponseEntity<RoomResponseDTO> create(@Valid @RequestBody RoomRequestDTO request) {
        RoomResponseDTO response = roomService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> findById(@PathVariable Long id) {
        RoomResponseDTO response = roomService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> update(@PathVariable Long id, @Valid @RequestBody RoomRequestDTO request) {
        RoomResponseDTO updatedRoom = roomService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAll() {
        List<RoomResponseDTO> rooms = roomService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rooms);
    }
}
