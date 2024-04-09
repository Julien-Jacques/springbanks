package com.example.banks;

import com.example.banks.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client findByNom(String nom) {
        return clientRepository.findByNom(nom);
    }

    public void save(Client nouveauClient) {
        clientRepository.save(nouveauClient);
    }

    public Client findById(Integer clientId) {
        Optional<Client> clientOptional = clientRepository.findById(Long.valueOf(clientId));
        return clientOptional.orElse(null);
    }
}
