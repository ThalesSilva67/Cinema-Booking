package com.booking.cinema.repository;

import com.booking.cinema.model.Booking;
import com.booking.cinema.model.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT CASE WHEN  COUNT(b) > 0 THEN true ELSE  false END
        FROM Booking b
        WHERE b.session.id = :sessionId
        AND 
        b.seatLabel = :seatLabel
        AND
        b.state NOT IN (:invalidStates)    
        """)
    boolean isSeatOccupied(@Param("sessionId") Long sessionId,
                         @Param("seatLabel") String seatLabel,
                         @Param("invalidStates") List<BookingState> invalidStates);
}
