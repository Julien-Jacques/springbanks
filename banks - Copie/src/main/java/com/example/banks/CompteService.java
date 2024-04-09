package com.example.banks;

import com.example.banks.repository.CompteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompteService {
    @Autowired
    private CompteRepository compteRepository;

    public List<Compte> findByClient(Client client) {
        return compteRepository.findByClient(client);
    }

    public List<Compte> findComptesByClientId(Integer clientId) {
        return compteRepository.findByClientId(clientId);
    }

    public void save(Compte nouveauCompte) {
        compteRepository.save(nouveauCompte);
    }

    public Compte findById(Long id) {
        Optional<Compte> compteOptional = compteRepository.findById(id);

        if (compteOptional.isPresent()) {
            return compteOptional.get();
        } else {
            throw new EntityNotFoundException("Compte non trouvé pour l'ID : " + id);
        }
    }

    public List<Compte> findAll() {
        return compteRepository.findAll();
    }
}
