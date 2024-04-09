package com.example.banks.repository;

import com.example.banks.Client;
import com.example.banks.Compte;
import com.example.banks.CompteService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    List<Compte> findByClient(Client client);

    List<Compte> findByClientId(Integer clientId);
}
