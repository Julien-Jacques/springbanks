package com.example.banks.controller;

import com.example.banks.*;
import com.fasterxml.jackson.databind.DatabindContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.banks.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

import static com.example.banks.IBANGenerator.generateIBAN;

@Controller
@RequestMapping("/compte")
public class CompteController {

    @Autowired
    private CompteService compteService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/ouvrir-compte")
    public String ouvrirCompte(HttpSession session, RedirectAttributes redirectAttributes) {
        Client client = (Client) session.getAttribute("client");
        System.out.println("client : " + client);
            if (client != null) {
                String iban = generateIBAN();
                Compte nouveauCompte = new Compte();
                nouveauCompte.setClient(client);
                nouveauCompte.setSolde(0);
                nouveauCompte.setDecouverteAutorise(false);
                nouveauCompte.setMontantdecouvert(Double.valueOf("0"));
                nouveauCompte.setIban(iban);
                compteService.save(nouveauCompte);

                redirectAttributes.addFlashAttribute("succes", "Nouveau compte bancaire ouvert avec succès.");
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Impossible d'ouvrir un compte. Client introuvable.");
            }
        return "redirect:/client/accueil";
    }
    @GetMapping("/modifier/{id}")
    public String afficherPageModifierCompte(@PathVariable("id") Long id, Model model) {
        Compte compte = compteService.findById(id);
        model.addAttribute("compte", compte);
        return "modifier";
    }
    @PostMapping("/modifier")
    public String modifierCompte(@RequestParam("compteId") Long compteId, @ModelAttribute("compte") Compte compteModifie, @RequestParam("montantDecouvert") Double montantDecouvert, RedirectAttributes redirectAttributes) {
        Compte existingCompte = compteService.findById(compteId);
        if (existingCompte != null) {
            existingCompte.setDecouverteAutorise(compteModifie.isDecouverteAutorise());
            existingCompte.setSolde(compteModifie.getSolde());
            existingCompte.setMontantdecouvert(montantDecouvert);

            compteService.save(existingCompte);

            redirectAttributes.addFlashAttribute("succes", "Compte modifié avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Impossible de trouver le compte à modifier.");
        }
        return "redirect:/client/accueil";
    }

    @PostMapping("/faire-virement")
    public String faireVirement(@RequestParam("montant") double montant,
                                @RequestParam("compteSource") Long idCompteSource,
                                @RequestParam("compteDestination") Long idCompteDestination,
                                RedirectAttributes redirectAttributes, HttpSession session) {
        Compte compteSource = compteService.findById(idCompteSource);
        Compte compteDestination = compteService.findById(idCompteDestination);

        if (compteSource == null || compteDestination == null) {
            redirectAttributes.addFlashAttribute("erreur", "Compte source ou compte de destination introuvable.");
            return "redirect:/client/accueil";
        }

        double soldeAvecDecouvert = compteSource.getSolde() + compteSource.getMontantdecouvert();

        if (montant > soldeAvecDecouvert) {
            redirectAttributes.addFlashAttribute("erreur", "Solde insuffisant sur le compte source.");
            return "redirect:/client/accueil";
        }

        compteSource.setSolde(compteSource.getSolde() - montant);
        compteDestination.setSolde(compteDestination.getSolde() + montant);

        compteService.save(compteSource);
        compteService.save(compteDestination);

        redirectAttributes.addFlashAttribute("succes", "Virement effectué avec succès.");
        Client client = (Client) session.getAttribute("client");
        transactionService.logTransaction(montant, "virement", client, compteSource, compteDestination);

        return "redirect:/client/accueil";
    }

    @PostMapping("/deposer")
    public String deposer(@RequestParam("montantDepot") double montantDepot,
                          @RequestParam("compteId") Long compteId,
                          RedirectAttributes redirectAttributes, HttpSession session) {
        Compte compte = compteService.findById(compteId);
        if (compte == null) {
            redirectAttributes.addFlashAttribute("erreur", "Compte introuvable.");
            return "redirect:/client/accueil";
        }

        compte.setSolde(compte.getSolde() + montantDepot);
        compteService.save(compte);

        redirectAttributes.addFlashAttribute("succes", "Dépôt effectué avec succès.");
        Client client = (Client) session.getAttribute("client");
        transactionService.logTransaction(montantDepot, "dépôt", client, compte);

        return "redirect:/client/accueil";
    }

    @PostMapping("/retirer")
    public String retirer(@RequestParam("montantRetrait") double montantRetrait,
                          @RequestParam("compteId") Long compteId,
                          RedirectAttributes redirectAttributes, HttpSession session) {
        Compte compte = compteService.findById(compteId);
        if (compte == null) {
            redirectAttributes.addFlashAttribute("erreur", "Compte introuvable.");
            return "redirect:/client/accueil";
        }

        if (compte.getSolde() < montantRetrait) {
            redirectAttributes.addFlashAttribute("erreur", "Solde insuffisant pour effectuer le retrait.");
            return "redirect:/client/accueil";
        }

        compte.setSolde(compte.getSolde() - montantRetrait);
        compteService.save(compte);

        redirectAttributes.addFlashAttribute("succes", "Retrait effectué avec succès.");
        Client client = (Client) session.getAttribute("client");
        transactionService.logTransaction(montantRetrait, "retrait", client, compte);

        return "redirect:/client/accueil";
    }



}