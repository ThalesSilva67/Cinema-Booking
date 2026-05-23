package com.booking.cinema.service;

import com.booking.cinema.dto.request.SessionRequestDTO;
import com.booking.cinema.dto.response.SessionResponseDTO;
import com.booking.cinema.model.Movie;
import com.booking.cinema.model.Room;
import com.booking.cinema.model.Session;
import com.booking.cinema.repository.MovieRepository;
import com.booking.cinema.repository.RoomRepository;
import com.booking.cinema.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public SessionService(SessionRepository sessionRepository,  MovieRepository movieRepository, RoomRepository roomRepository) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    public SessionResponseDTO save(SessionRequestDTO sessionDTO) {
        Room room = roomRepository.findById(sessionDTO.roomId()).orElseThrow(() -> new IllegalArgumentException("Room not found!"));
        Movie movie = movieRepository.findById(sessionDTO.movieId()).orElseThrow(() -> new IllegalArgumentException("Movie not found!"));

        Session sessionEntity = new Session();
        sessionEntity.setRoom(room);
        sessionEntity.setMovieId(movie);
        sessionEntity.setPrice(sessionDTO.price());
        sessionEntity.setStartTime(sessionDTO.startTime());

        Session saved = sessionRepository.save(sessionEntity);

        return new SessionResponseDTO(saved.getId(), saved.getRoom().getId(), saved.getMovieId().getId(), saved.getStartTime(), saved.getPrice());
    }

    public List<SessionResponseDTO> findAll() {
        List<Session> list = sessionRepository.findAll();

        if (list.isEmpty()) return new ArrayList<>();

        List<SessionResponseDTO> listDto = new ArrayList<>();
        for (Session session : list) {
            listDto.add(new SessionResponseDTO(session.getId(), session.getRoom().getId(), session.getMovieId().getId(), session.getStartTime(), session.getPrice()));
        }

        return listDto;
    }

    public SessionResponseDTO findById(Long id) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + id));

        return new SessionResponseDTO(session.getId(), session.getRoom().getId(), session.getMovieId().getId(), session.getStartTime(), session.getPrice());
    }

    public void delete(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new EntityNotFoundException("Session not found");
        }

        sessionRepository.deleteById(id);
    }

    public SessionResponseDTO update(Long id, SessionRequestDTO session) {
        Session oldSession = sessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Session not found"));
        Room room = roomRepository.findById(session.roomId()).orElseThrow(() -> new IllegalArgumentException("Room not found!"));
        Movie movie = movieRepository.findById(session.movieId()).orElseThrow(() -> new IllegalArgumentException("Movie not found!"));

        oldSession.setMovieId(movie);
        oldSession.setRoom(room);
        oldSession.setStartTime(session.startTime());
        oldSession.setPrice(session.price());

        Session update = sessionRepository.save(oldSession);

        return new SessionResponseDTO(update.getId(), update.getRoom().getId(), update.getMovieId().getId(), update.getStartTime(), update.getPrice());
    }
}
