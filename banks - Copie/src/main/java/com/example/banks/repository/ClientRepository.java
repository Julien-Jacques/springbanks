package com.example.banks.repository;

import com.example.banks.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByNom(String nom);
}
