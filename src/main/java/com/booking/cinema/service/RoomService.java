package com.booking.cinema.service;

import com.booking.cinema.dto.request.RoomRequestDTO;
import com.booking.cinema.dto.response.RoomResponseDTO;
import com.booking.cinema.model.Room;
import com.booking.cinema.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private static final int MAX_ROOM_CAPACITY = 500;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomResponseDTO save(RoomRequestDTO dto) {
        int capacity = dto.totalRows() * dto.seatPerRow();
        if(capacity > MAX_ROOM_CAPACITY) {
            throw new IllegalArgumentException("A sala excede a capacidade máxima de " + MAX_ROOM_CAPACITY + " lugares.");
        }

        Room room = new Room();
        room.setName(dto.name());
        room.setTotalRows(dto.totalRows());
        room.setSeatPerRow(dto.seatPerRow());
        Room saved = roomRepository.save(room);

        return new RoomResponseDTO(saved.getId(), saved.getName(), saved.getTotalRows(), saved.getSeatPerRow());
    }

    public List<RoomResponseDTO> findAll() {
        List<Room> list = roomRepository.findAll();

        if (list.isEmpty()) return new ArrayList<>();

        List<RoomResponseDTO> listDto = new ArrayList<>();
        for (Room room : list) {
            listDto.add(new RoomResponseDTO(room.getId(), room.getName(), room.getTotalRows(), room.getSeatPerRow()));
        }

        return listDto;
    }

    public RoomResponseDTO findById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + id));

        return new RoomResponseDTO(room.getId(),room.getName(),room.getTotalRows(), room.getSeatPerRow());
    }

    public void delete(Long id) {
        if(!roomRepository.existsById(id)){
            throw new EntityNotFoundException("Room not found");
        }

        roomRepository.deleteById(id);
    }

    public RoomResponseDTO update(Long id, RoomRequestDTO room) {
        Room oldRoom =  roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + id));

        oldRoom.setName(room.name());
        oldRoom.setTotalRows(room.totalRows());
        oldRoom.setSeatPerRow(room.seatPerRow());

        Room update = roomRepository.save(oldRoom);

        return new RoomResponseDTO(update.getId(), update.getName(), update.getTotalRows(), update.getSeatPerRow());
    }
}
