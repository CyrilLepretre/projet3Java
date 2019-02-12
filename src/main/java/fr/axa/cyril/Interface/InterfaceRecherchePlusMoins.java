package fr.axa.cyril.Interface;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
import fr.axa.cyril.Menu.Configuration;

import java.util.Scanner;

public class InterfaceRecherchePlusMoins extends InterfaceJeu {
    private Scanner scanner;
    private String saisieUtilisateur;
    /*private String derniereCombinaisonProposee;
    protected String derniereReponseUtilisateur;*/
    protected boolean jeuTermine;

    public void lancerJeu(int modeDeJeu, Configuration recherchePlusMoinsConf) {
        RecherchePlusMoins recherchePlusMoins = new RecherchePlusMoins(recherchePlusMoinsConf);
        RecherchePlusMoins recherchePlusMoins2 = new RecherchePlusMoins(recherchePlusMoinsConf);

        //recherchePlusMoins.initialiser(modeDeJeu);
        String combinaisonAtrouver = "";
        if ((modeDeJeu == 1) || (modeDeJeu == 3)) {
            combinaisonAtrouver = recherchePlusMoins.genererCombinaison(recherchePlusMoins.getConfiguration().getTailleCombinaison());
            if (recherchePlusMoinsConf.getModeDebug()) {
                System.out.println("## Mode Debug ##    Combinaison à trouver : " + combinaisonAtrouver);
            }
        }
        derniereCombinaisonProposee = "";
        derniereReponseUtilisateur = "";
        jeuTermine = false;
        int numeroTour = 0;
        //System.out.println("#####" + recherchePlusMoins.getNombreEssaisRestants());
        //System.out.println(jeuTermine);
        while ((recherchePlusMoins.getNombreEssaisRestants() > 0) && (!jeuTermine))//(!(recherchePlusMoins.getJeuFini())) && (!finModeDuel))
        {
            switch (modeDeJeu) {
                case 1 :
                    jouerTourModeChallenger(combinaisonAtrouver, recherchePlusMoins);
                    break;
                case 2 :
                    jouerTourModeDefenseur(derniereCombinaisonProposee, derniereReponseUtilisateur, recherchePlusMoins);
                    break;
                case 3 :
                    numeroTour++;
                    jouerTourModeDuel(numeroTour, combinaisonAtrouver, recherchePlusMoins, derniereCombinaisonProposee, derniereReponseUtilisateur, recherchePlusMoins2);
                    break;
            }
        }
        if ((recherchePlusMoins.getNombreEssaisRestants() == 0) && (!jeuTermine)) {
            System.out.println("Dommage, vous avez perdu");
            if (modeDeJeu == 1) {
                System.out.println("La combinaison à trouver était : " + combinaisonAtrouver);
            }
        }
    }

    /*private void jouerTourModeChallenger(String combinaisonAtrouver, RecherchePlusMoins recherchePlusMoins) {
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            System.out.println("Indiquez votre proposition de "+recherchePlusMoins.getConfiguration().getTailleCombinaison()+" caractères parmi "+ recherchePlusMoins.getConfiguration().getListeValeursPossibles() + " [il vous reste "+recherchePlusMoins.getNombreEssaisRestants()+" essai(s)]");
            scanner = new Scanner(System.in);
            saisieUtilisateur = scanner.nextLine();
            saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateur(saisieUtilisateur, "chiffres", recherchePlusMoins.getConfiguration().getTailleCombinaison(), recherchePlusMoins.getConfiguration().getListeValeursPossibles());
            numeroSaisie++;
        }
        if (recherchePlusMoins.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver, recherchePlusMoins.getNombreEssaisRestants())) {
            System.out.println("Félicitations, vous avez gagné en " + (recherchePlusMoins.getConfiguration().getMaxEssais() - recherchePlusMoins.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
        else {
            System.out.println("Proposition : " + saisieUtilisateur + " -> Réponse : "+recherchePlusMoins.calculerReponseCombinaison(saisieUtilisateur, combinaisonAtrouver));
        }
    }*/

    /*private void jouerTourModeDefenseur(String derniereCombinaisonProposee, String derniereReponseUtilisateur, RecherchePlusMoins recherchePlusMoins) {
        this.derniereCombinaisonProposee = recherchePlusMoins.proposerCombinaison(derniereCombinaisonProposee,derniereReponseUtilisateur);
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        System.out.println("Voici ma proposition : " + this.derniereCombinaisonProposee);
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            System.out.println("Evaluez chaque chiffre de la proposition faite en indiquant (sans les crochets) [=] si le chiffre est correct, [+} si le chiffre est supérieur, [-] si le chiffre est inférieur");
            scanner = new Scanner(System.in);
            saisieUtilisateur = scanner.nextLine();
            this.derniereReponseUtilisateur = saisieUtilisateur;
            saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateurPlusMoinsDefenseur(saisieUtilisateur, recherchePlusMoins.getConfiguration().getTailleCombinaison(), "+-=");
            numeroSaisie++;
        }
        if (recherchePlusMoins.determinerSiFinJeu(saisieUtilisateur)) {
            System.out.println("J'ai trouvé la réponse en " + (recherchePlusMoins.getConfiguration().getMaxEssais() - recherchePlusMoins.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
    } */

    private void jouerTourModeDuel(int numeroTour, String combinaisonAtrouver, RecherchePlusMoins recherchePlusMoinsChallenger, String derniereCombinaisonProposee, String derniereReponseUtilisateur, RecherchePlusMoins recherchePlusMoinsDefenseur) {
        System.out.println("***** Tour " + numeroTour + " *****");
        System.out.println("--> A votre tour");
        jouerTourModeChallenger(combinaisonAtrouver, recherchePlusMoinsChallenger);
        System.out.println("--> A mon tour");
        jouerTourModeDefenseur(derniereCombinaisonProposee, derniereReponseUtilisateur, recherchePlusMoinsDefenseur);

        if (recherchePlusMoinsDefenseur.getJeuFini() && recherchePlusMoinsChallenger.getJeuFini()) {
            System.out.println("Egalité ! ");
        }
        else if (recherchePlusMoinsChallenger.getJeuFini()) {
            System.out.println("Bravo, vous m'avez battu ;-)");
        }
        else if (recherchePlusMoinsDefenseur.getJeuFini()) {
            System.out.println("J'ai gagné ;-)");
        }
    }
}
