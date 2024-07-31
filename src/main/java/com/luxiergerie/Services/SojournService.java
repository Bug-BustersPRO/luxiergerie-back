package com.luxiergerie.Services;

import com.luxiergerie.DTO.SojournDTO;
import com.luxiergerie.Domain.Entity.Client;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Mapper.SojournMapper;
import com.luxiergerie.Domain.Repository.ClientRepository;
import com.luxiergerie.Domain.Repository.RoleRepository;
import com.luxiergerie.Domain.Repository.RoomRepository;
import com.luxiergerie.Domain.Repository.SojournRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.luxiergerie.Domain.Enums.SojournStatus.*;
import static java.util.Objects.isNull;

@Service
public class SojournService {
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final SojournRepository sojournRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final SMSService smsService;

    public SojournService(ClientRepository clientRepository, RoomRepository roomRepository, SojournRepository sojournRepository, RoleRepository roleRepository, EmailService emailService, SMSService smsService) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.sojournRepository = sojournRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public Client getClient(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
    }

    public Room getRoom(String roleName, LocalDateTime entryDate, LocalDateTime exitDate) {
        List<Room> rooms = roomRepository.findAvailableRoomsByRoleName(
                roleName,
                entryDate,
                exitDate,
                PageRequest.of(0, 1)
        );
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.getFirst();
    }

    @Transactional
    public void createSojourn(SojournDTO sojournDTO) {

        LocalDateTime adjustedEntryDate = sojournDTO.getEntryDate().atTime(LocalTime.of(14, 0));
        LocalDateTime adjustedExitDate = sojournDTO.getExitDate().atTime(LocalTime.of(11, 0));

        Sojourn sojourn = SojournMapper.MappedSojournFrom(sojournDTO);

        Client client = this.getClient(sojournDTO.getClientId());
        Room room = this.getRoom(sojournDTO.getRoomRole().getName(), adjustedEntryDate, adjustedExitDate);

        if (isNull(room)) {
            throw new RuntimeException("There are no rooms available for the selected dates.");
        }

        int pin = new Random().nextInt(9000) + 1000;
        sojourn.setPin(pin);

        sojourn.setStatus(RESERVED);

        int identifiedUnique = new Random().nextInt(9000) + 1000;

        String sojournIdentifier =
                client.getFirstName().substring(0, 1) + client.getLastName().substring(0, 1) +
                        room.getRoomNumber() + adjustedEntryDate.getDayOfMonth() +
                        adjustedEntryDate.getMonthValue() + adjustedEntryDate.getYear() +
                        identifiedUnique;

        sojourn.setSojournIdentifier(sojournIdentifier);
        sojourn.setClient(client);
        sojourn.setRoom(room);

        client.getSojourns().add(sojourn);
        room.getSojourns().add(sojourn);
        room.setClient(client);

        this.sojournRepository.save(sojourn);
        this.clientRepository.save(client);
        this.roomRepository.save(room);

        // Send email
        //String emailSubject = "Your Sojourn Credentials";
        //String emailBody = "Your new PIN is: " + sojourn.getPin() + " and your identifier is: " + sojourn.getSojournIdentifier();
        //emailService.sendEmail(sojourn.getClient().getEmail(), emailSubject, emailBody);
    }


    @Transactional
    public Sojourn updateSojourn(UUID sojournId, SojournDTO sojournDTO) {
        Sojourn sojourn = this.sojournRepository.findById(sojournId).orElseThrow(() -> new RuntimeException("Sojourn not found with id: " + sojournId));
        Room room = this.roomRepository.findById(sojournDTO.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found with id: " + sojournDTO.getRoomId()));
        LocalDateTime adjustedEntryDate = sojournDTO.getEntryDate().atTime(LocalTime.of(14, 0));
        LocalDateTime adjustedExitDate = sojournDTO.getExitDate().atTime(LocalTime.of(11, 0));
        sojourn.setEntryDate(adjustedEntryDate);
        sojourn.setExitDate(adjustedExitDate);
        sojourn.setStatus(sojournDTO.getStatus());
        sojourn.setClient(this.getClient(sojournDTO.getClientId()));
        sojourn.setRoom(room);
        room.setClient(this.clientRepository.findById(sojournDTO.getClientId()).get());
        this.sojournRepository.save(sojourn);
        return sojourn;
    }

    @Transactional
    public void deleteSojourn(UUID sojournId) {
        if (!this.sojournRepository.existsById(sojournId)) {
            throw new RuntimeException("Sojourn not found with id: " + sojournId);
        }
        Room room = this.sojournRepository.findById(sojournId).get().getRoom();
        room.setClient(null);
        this.sojournRepository.deleteById(sojournId);
    }

    @Transactional
    public void recoverPasswordAndIdentifier(UUID sojournId) {
        Sojourn sojourn = this.sojournRepository.findById(sojournId)
                .orElseThrow(() -> new RuntimeException("Sojourn not found with id: " + sojournId));

        int newPin = new Random().nextInt(9000) + 1000;
        sojourn.setPin(newPin);

        this.sojournRepository.save(sojourn);

        SojournDTO sojournDTO = new SojournDTO();
        sojournDTO.setPin(sojourn.getPin());
        sojournDTO.setSojournIdentifier(sojourn.getSojournIdentifier());

        // Send email
        //String emailSubject = "Your Sojourn Credentials";
        //String emailBody = "Your new PIN is: " + sojourn.getPin() + " and your identifier is: " + sojourn.getSojournIdentifier();
        //emailService.sendEmail(sojourn.getClient().getEmail(), emailSubject, emailBody);

        // Send SMS it cost me money
        /*String smsBody = "Your new PIN is: " + sojourn.getPin() + " and your identifier is: " + sojourn.getSojournIdentifier();
        smsService.sendSMS(sojourn.getClient().getPhoneNumber(), smsBody);*/
    }

    @Transactional
    public Sojourn cancelSojourn(UUID sojournId) {
        Sojourn sojourn = this.sojournRepository.findById(sojournId)
                .orElseThrow(() -> new RuntimeException("Sojourn not found with id: " + sojournId));
        sojourn.setStatus(CANCELLED);
        this.sojournRepository.save(sojourn);
        return sojourn;
    }


    @Scheduled(cron = "0 0 14 * * *")
    public void makeSojournActive() {
        List<Sojourn> sojourns = this.sojournRepository.findAll();
        for (Sojourn sojourn : sojourns) {
            if (sojourn.getEntryDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
                    sojourn.getEntryDate().getMonthValue() == LocalDateTime.now().getMonthValue() &&
                    sojourn.getEntryDate().getYear() == LocalDateTime.now().getYear()) {
                sojourn.setStatus(IN_PROGRESS);
                this.sojournRepository.save(sojourn);
            }
        }
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void makeSojournInactive() {
        List<Sojourn> sojourns = this.sojournRepository.findAll();
        for (Sojourn sojourn : sojourns) {
            if (sojourn.getExitDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
                    sojourn.getExitDate().getMonthValue() == LocalDateTime.now().getMonthValue() &&
                    sojourn.getExitDate().getYear() == LocalDateTime.now().getYear()) {
                sojourn.setStatus(FINISHED);
                this.sojournRepository.save(sojourn);
            }
        }
    }
}
