package fr.axa.cyril.Interface;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Jeu.Jeu;
import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
import fr.axa.cyril.Menu.Configuration;
import java.util.Scanner;

public class InterfaceJeu {

    private Scanner scanner;
    private String saisieUtilisateur;
    private boolean jeuTermine;
    private String derniereCombinaisonProposee;
    private String derniereReponseUtilisateur;
    private int jeuChoisi;
    private int numeroTour;

    public void lancerJeu(int jeuChoisi, int modeDeJeu, Configuration configuration) {
        Jeu partieJeu1;
        Jeu partieJeu2;
        this.jeuChoisi = jeuChoisi;
        if (jeuChoisi == 1) {
            partieJeu1 = new Mastermind(configuration);
            partieJeu2 = new Mastermind(configuration);
        }
        else {
            partieJeu1 = new RecherchePlusMoins(configuration);
            partieJeu2 = new RecherchePlusMoins(configuration);
        }
        String combinaisonAtrouver = "";
        if ((modeDeJeu == 1) || (modeDeJeu == 3)) {
            combinaisonAtrouver = partieJeu1.genererCombinaison(partieJeu1.getConfiguration().getTailleCombinaison());
            if (configuration.getModeDebug()) {
                System.out.println("## Mode Debug ##    Combinaison à trouver : " + combinaisonAtrouver);
            }
        }
        derniereCombinaisonProposee = "";
        derniereReponseUtilisateur = "";
        jeuTermine = false;
        numeroTour = 0;
        while ((partieJeu1.getNombreEssaisRestants() > 0) && (!jeuTermine))
        {
            switch (modeDeJeu) {
                case 1 :
                    jouerTourModeChallenger(combinaisonAtrouver, partieJeu1);
                    break;
                case 2 :
                    jouerTourModeDefenseur(derniereCombinaisonProposee, derniereReponseUtilisateur, partieJeu1);
                    break;
                case 3 :
                    jouerTourModeDuel(numeroTour, combinaisonAtrouver, partieJeu1, derniereCombinaisonProposee, derniereReponseUtilisateur, partieJeu2);
                    break;
            }
            numeroTour++;
        }
        if ((partieJeu1.getNombreEssaisRestants() == 0) && (!jeuTermine)) {
            if (modeDeJeu == 1) {
                System.out.println("Dommage, vous avez perdu");
                System.out.println("La combinaison à trouver était : " + combinaisonAtrouver);
            } else if (modeDeJeu == 2) {
                System.out.println("Dommage, j'ai perdu");
            }
        }
    }

    private void jouerTourModeChallenger(String combinaisonAtrouver, Jeu jeu) {
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            System.out.println("Indiquez votre proposition de "+jeu.getConfiguration().getTailleCombinaison()+" caractères parmi "+ jeu.getConfiguration().getListeValeursPossibles() + " [il vous reste "+jeu.getNombreEssaisRestants()+" essai(s)]");
            scanner = new Scanner(System.in);
            saisieUtilisateur = scanner.nextLine();
            if ((this.jeuChoisi == 2)) {
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateurGenerique(saisieUtilisateur, jeu.getConfiguration().getTailleCombinaison(), jeu.getConfiguration().getListeValeursPossibles());
            }
            else {
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateurGenerique(saisieUtilisateur, jeu.getConfiguration().getTailleCombinaison(), jeu.getConfiguration().getListeValeursPossibles());
            }
            numeroSaisie++;
        }
        if (jeu.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver)) {
            System.out.println("Félicitations, vous avez gagné en " + (jeu.getConfiguration().getMaxEssais() - jeu.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
        else {
            System.out.println("Proposition : " + saisieUtilisateur + " -> Réponse : "+ jeu.calculerReponseCombinaison(saisieUtilisateur, combinaisonAtrouver));
        }
    }

    private void jouerTourModeDefenseur(String derniereCombinaisonProposee, String derniereReponseUtilisateur, Jeu jeu) {
        if (this.numeroTour == 0) {
            if (jeuChoisi == 1) {
                System.out.println("--> Veuillez choisir une combinaison de " + jeu.getConfiguration().getTailleCombinaison() + " caractères parmi la liste : " +jeu.getConfiguration().getListeValeursPossibles());
            }
            else {
                System.out.println("--> Veuillez choisir une combinaison de " + jeu.getConfiguration().getTailleCombinaison() + " chiffres parmi la liste : " +jeu.getConfiguration().getListeValeursPossibles());
            }
        }
        this.derniereCombinaisonProposee = jeu.proposerCombinaison(derniereCombinaisonProposee,derniereReponseUtilisateur);
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        System.out.println("Voici ma proposition : " + this.derniereCombinaisonProposee + "    [" + jeu.getNombreEssaisRestants() + " essais restant(s)]");
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu ou votre réponse n'est pas cohérente.");
            }
            if (this.jeuChoisi == 2) {
                System.out.println("Evaluez chaque chiffre de la proposition faite en indiquant (sans les crochets) [=] si le chiffre est correct, [+} si le chiffre est supérieur, [-] si le chiffre est inférieur");
                scanner = new Scanner(System.in);
                saisieUtilisateur = scanner.nextLine();
                this.derniereReponseUtilisateur = saisieUtilisateur;
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateurGenerique(saisieUtilisateur, jeu.getConfiguration().getTailleCombinaison(), "+-=");
                if (saisieCorrecte) {
                    saisieCorrecte = jeu.verifierErreurFonctionnelle(saisieUtilisateur);
                }
                numeroSaisie++;
            }
            else {
                System.out.println("Combien de couleurs sont à la bonne place : ");
                scanner = new Scanner(System.in);
                String valeursOK = scanner.nextLine();
                System.out.println("Combien de couleurs sont à la mauvaise place : ");
                String valeursKO = scanner.nextLine();
                saisieUtilisateur = valeursOK + "-" + valeursKO;
                this.derniereReponseUtilisateur = saisieUtilisateur;
                StringBuilder reponsesEvalMastermind = new StringBuilder();
                for (int i = 0; i <= jeu.getConfiguration().getTailleCombinaison(); i++) {reponsesEvalMastermind.append(i);}
                saisieCorrecte = new ControleSaisie().controlerSaisieMastermindDefenseur(saisieUtilisateur, 3, reponsesEvalMastermind.toString());
                numeroSaisie++;
            }
        }
        if (jeu.determinerSiFinJeu(saisieUtilisateur)) {
            System.out.println("J'ai trouvé la réponse en " + (jeu.getConfiguration().getMaxEssais() - jeu.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
    }

    private void jouerTourModeDuel(int numeroTour, String combinaisonAtrouver, Jeu jeuChallenger, String derniereCombinaisonProposee, String derniereReponseUtilisateur, Jeu jeuDefenseur) {
        System.out.println("***** Tour " + numeroTour + " *****");
        System.out.println("--> A votre tour");
        jouerTourModeChallenger(combinaisonAtrouver, jeuChallenger);
        System.out.println("--> A mon tour");
        jouerTourModeDefenseur(derniereCombinaisonProposee, derniereReponseUtilisateur, jeuDefenseur);

        if (jeuDefenseur.getJeuFini() && jeuChallenger.getJeuFini()) {
            System.out.println("Egalité ! ");
        }
        else if (jeuChallenger.getJeuFini()) {
            System.out.println("Bravo, vous m'avez battu ;-)");
        }
        else if (jeuDefenseur.getJeuFini()) {
            System.out.println("J'ai gagné ;-)");
        }
    }
}