package com.example.banks;

import com.example.banks.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void logTransaction(double montant, String type, Client client, Compte... comptes) {
        Transaction transaction = new Transaction();
        transaction.setMontant(montant);
        transaction.setType(type);
        transaction.setDate(LocalDateTime.now());
        transaction.setClient(client);

        StringBuilder details = new StringBuilder();
        if (comptes != null && comptes.length > 0) {
            for (Compte compte : comptes) {
                details.append("Compte: ").append(compte.getIban()).append(", ");
            }
            details.deleteCharAt(details.length() - 1); // Remove the last comma
        }

        transaction.setDetails(details.toString());

        transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> findByClient(Client client) {
        return transactionRepository.findByClient(client);
    }
}