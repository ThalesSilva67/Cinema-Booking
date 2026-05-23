package com.booking.cinema.service;

import com.booking.cinema.dto.request.MovieRequestDTO;
import com.booking.cinema.dto.response.MovieResponseDTO;
import com.booking.cinema.model.Movie;
import com.booking.cinema.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponseDTO save(MovieRequestDTO movie) {
        boolean present = movieRepository.findByTitle(movie.title()).isPresent();

        if (present) throw new IllegalArgumentException("Movie already exists");

        Movie movieEntity = new Movie();
        movieEntity.setTitle(movie.title());
        movieEntity.setDescription(movie.description());
        movieEntity.setDuration(movie.duration());

        Movie save = movieRepository.save(movieEntity);

        return new MovieResponseDTO(save.getId(), save.getTitle(), save.getDescription(), save.getDuration());
    }

    public List<MovieResponseDTO> findAll() {
        List<Movie> list = movieRepository.findAll();

        if (list.isEmpty()) return new ArrayList<>();

        List<MovieResponseDTO> listDto = new ArrayList<>();
        for (Movie movie : list) {
            listDto.add(new MovieResponseDTO(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getDuration()));
        }

        return listDto;
    }

    public MovieResponseDTO findById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found with ID: " + id));

        return new MovieResponseDTO(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getDuration());
    }

    public void delete(Long id) {
        if(!movieRepository.existsById(id)){
            throw new EntityNotFoundException("Movie not found");
        }

        movieRepository.deleteById(id);
    }

    public MovieResponseDTO update(Long id, MovieRequestDTO movie) {
        Movie oldMovie =  movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        oldMovie.setTitle(movie.title());
        oldMovie.setDescription(movie.description());
        oldMovie.setDuration(movie.duration());

        Movie update = movieRepository.save(oldMovie);

        return new MovieResponseDTO(update.getId(), update.getTitle(), update.getDescription(), update.getDuration());
    }

}
