package com.luxiergerie.Services;

import com.luxiergerie.Repository.ClientRepository;
import com.luxiergerie.Repository.RoleRepository;
import com.luxiergerie.Repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;
    private final RoleRepository roleRepository;
    private final SMSService smsService;
    private final EmailService emailService;

    public ClientService(ClientRepository clientRepository,
                         RoomRepository roomRepository,
                         RoleRepository roleRepository,
                         SMSService smsService,
                         EmailService emailService) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
        this.roleRepository = roleRepository;
        this.smsService = smsService;
        this.emailService = emailService;
    }

}
