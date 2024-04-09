package com.example.banks.controller;

import com.example.banks.*;
import com.example.banks.repository.CompteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CompteService compteService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/connexion")
    public String afficherPageConnexion() {
        return "connexion";
    }

    @PostMapping("/connexion")
    public String seConnecter(@RequestParam("nom") String nom, @RequestParam("motDePasse") String motDePasse, RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println("Nom d'utilisateur : " + nom);
        System.out.println("Mot de passe : " + motDePasse);
        if (nom != null && motDePasse != null) {
            Client client = clientService.findByNom(nom);
            if (client != null) {
                System.out.println("Nom d'utilisateur trouvé dans la base de données : " + client.getNom());
                System.out.println("Mot de passe trouvé dans la base de données : " + client.getMotDePasse());
                if (motDePasse.equals(client.getMotDePasse())) {
                    session.setAttribute("client", client);
                    return "redirect:/client/accueil";
                } else {
                    redirectAttributes.addFlashAttribute("erreur", "Mot de passe incorrect.");
                    return "redirect:/client/connexion";
                }
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Nom d'utilisateur non trouvé.");
                return "redirect:/client/connexion";
            }
        }
        redirectAttributes.addFlashAttribute("erreur", "Veuillez saisir un nom d'utilisateur et un mot de passe.");
        return "redirect:/client/connexion";
    }





    @PostMapping("/creer-compte")
    public String creerCompte(@RequestParam("nomNouveau") String nom,
                              @RequestParam("motDePasseNouveau") String motDePasse,
                              RedirectAttributes redirectAttributes) {
        Client existingClient = clientService.findByNom(nom);
        if (existingClient != null) {
            redirectAttributes.addFlashAttribute("erreur", "Ce nom d'utilisateur est déjà utilisé.");
            return "redirect:/client/connexion";
        } else {
            Client nouveauClient = new Client();
            nouveauClient.setNom(nom);
            nouveauClient.setMotDePasse(motDePasse);

            clientService.save(nouveauClient);

            redirectAttributes.addFlashAttribute("succes", "Compte créé avec succès. Vous pouvez maintenant vous connecter.");

            return "redirect:/client/connexion";
        }
    }

    @GetMapping("/accueil")
    public String afficherAccueil(HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/client/connexion";
        }
        List<Compte> comptes = compteService.findByClient(client);
        List<Transaction> transactions = transactionService.findByClient(client);
        double soldeTotal = comptes.stream().mapToDouble(Compte::getSolde).sum();
        List<Compte> tousLesComptes = compteService.findAll();
        model.addAttribute("tousLesComptes", tousLesComptes);
        model.addAttribute("client", client);
        model.addAttribute("comptes", comptes);
        model.addAttribute("soldeTotal", soldeTotal);
        model.addAttribute("transactions", transactions);
        return "accueil";
    }



}

