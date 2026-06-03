package com.booking.cinema.service;

import com.booking.cinema.dto.request.BookingRequestDTO;
import com.booking.cinema.dto.response.BookingResponseDTO;
import com.booking.cinema.exception.BusinessRuleException;
import com.booking.cinema.model.*;
import com.booking.cinema.repository.BookingRepository;
import com.booking.cinema.repository.SessionRepository;
import com.booking.cinema.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        Session session = sessionRepository.findById(bookingRequestDTO.sessionId()).orElseThrow(() -> new BusinessRuleException("Session not found!"));

        if (!session.getStartTime().isAfter(LocalDateTime.now())) {
            throw new BusinessRuleException("Session unavailable!");
        }

        validateSeat(bookingRequestDTO.seatLabel(), session.getRoom());

        List<BookingState> excludedStates = List.of(BookingState.CANCELED, BookingState.EXPIRED);
        boolean seatOccupied = bookingRepository.isSeatOccupied(session.getId(), bookingRequestDTO.seatLabel(), excludedStates);

        if (seatOccupied) {
            throw new BusinessRuleException("Seat number " + bookingRequestDTO.seatLabel() + " occupied!");
        }

        BigDecimal price = (bookingRequestDTO.ticket() == TypeTicket.MEIA_ENTRADA) ? session.getPrice().multiply(BigDecimal.valueOf(0.5)) : session.getPrice();

        Users user = userRepository.findById(bookingRequestDTO.userId()).orElseThrow(() -> new BusinessRuleException("User not found!"));

        Booking booking = new Booking();
        booking.setSeatLabel(bookingRequestDTO.seatLabel());
        booking.setSession(session);
        booking.setUser(user);
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        booking.setPrice(price);
        booking.setState(BookingState.PENDING);
        booking.setTicket(bookingRequestDTO.ticket());

        Booking saved = bookingRepository.save(booking);
        return new BookingResponseDTO(saved.getId(), saved.getSession().getId(), saved.getUser().getId(), saved.getSeatLabel(), saved.getPrice(), saved.getState(), saved.getTicket());
    }

    public List<BookingResponseDTO> findAll() {
        List<Booking> list = bookingRepository.findAll();

        if (list.isEmpty()) return new ArrayList<>();

        List<BookingResponseDTO> listDto = new ArrayList<>();
        for (Booking booking : list) {
            listDto.add(new BookingResponseDTO(booking.getId(), booking.getSession().getId(), booking.getUser().getId(), booking.getSeatLabel(), booking.getPrice(), booking.getState(), booking.getTicket()));
        }

        return listDto;
    }

    public BookingResponseDTO findById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BusinessRuleException("booking not found with ID: " + id));

        return new BookingResponseDTO(booking.getId(), booking.getSession().getId(), booking.getUser().getId(), booking.getSeatLabel(), booking.getPrice(), booking.getState(), booking.getTicket());
    }

    public void delete(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BusinessRuleException("booking not found");
        }

        bookingRepository.deleteById(id);
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cleanUpExpiredBooking() {
        int canceledCount = bookingRepository.cancelExpiredBooking(LocalDateTime.now());
        if (canceledCount > 0) {
            System.out.println("Faxina automática: " + canceledCount + " ingressos expirados foram liberados!");
        }
    }

    public void confirmBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BusinessRuleException("booking not found with ID: " + bookingId));
        booking.setState(BookingState.APPROVED);
    }

    public void cancelBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BusinessRuleException("booking not found with ID: " + bookingId));
        booking.setState(BookingState.CANCELED);
    }

    private void validateSeat(String seatLabel, Room room) {
        if (seatLabel == null || seatLabel.length() < 2) {
            throw new BusinessRuleException("Invalid seat label format!");
        }

        char rowLetter = seatLabel.toUpperCase().charAt(0);
        int rowIndex = (rowLetter - 'A') + 1;

        if (rowIndex > room.getTotalRows() || rowIndex < 1) {
            throw new BusinessRuleException("Invalid row number!");
        }

        try {
            int seatNumber = Integer.parseInt(seatLabel.substring(1));
            if (seatNumber > room.getSeatPerRow() || seatNumber < 1) {
                throw new BusinessRuleException("Invalid seat number for this room!");
            }
        } catch (NumberFormatException e) {
            throw new BusinessRuleException("Seat number must be a valid integer! " + e.getMessage());
        }
    }

}
