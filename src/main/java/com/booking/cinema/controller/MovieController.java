package com.booking.cinema.controller;

import com.booking.cinema.dto.request.MovieRequestDTO;
import com.booking.cinema.dto.response.MovieResponseDTO;
import com.booking.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping()
    public ResponseEntity<MovieResponseDTO> create(@Valid @RequestBody MovieRequestDTO request) {
        MovieResponseDTO response = movieService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> findById(@PathVariable Long id) {
        MovieResponseDTO response = movieService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MovieRequestDTO request) {
        MovieResponseDTO updatedMovie = movieService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDTO>> getAll() {
        List<MovieResponseDTO> movies = movieService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(movies);
    }
}
