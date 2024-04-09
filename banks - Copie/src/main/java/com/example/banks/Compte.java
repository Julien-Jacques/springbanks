package com.example.banks;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
@Table(name = "compte")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iban;

    @Column(name = "decouverautorise")
    private boolean decouverteAutorise;

    private Double montantdecouvert;

    private double solde;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Compte() {
    }

    public Compte(String iban, boolean decouverteAutorise, double solde, Client client) {
        this.iban = iban;
        this.decouverteAutorise = decouverteAutorise;
        this.solde = solde;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public double getMontantdecouvert() {
        return montantdecouvert;
    }

    public void setMontantdecouvert(Double montantdecouvert) {
        this.montantdecouvert = montantdecouvert;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public boolean isDecouverteAutorise() {
        return decouverteAutorise;
    }

    public void setDecouverteAutorise(boolean decouverteAutorise) {
        this.decouverteAutorise = decouverteAutorise;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}