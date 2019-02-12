package fr.axa.cyril.Interface;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Jeu.Jeu;
import fr.axa.cyril.Menu.Configuration;

import java.util.Scanner;

public abstract class InterfaceJeu {

    private Scanner scanner;
    private String saisieUtilisateur;
    protected boolean jeuTermine;
    protected String derniereCombinaisonProposee;
    protected String derniereReponseUtilisateur;

    abstract void lancerJeu(int modeDeJeu, Configuration configurationJeu);

    protected void jouerTourModeChallenger(String combinaisonAtrouver, Jeu jeu) {
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            System.out.println("Indiquez votre proposition de "+jeu.getConfiguration().getTailleCombinaison()+" caractères parmi "+ jeu.getConfiguration().getListeValeursPossibles() + " [il vous reste "+jeu.getNombreEssaisRestants()+" essai(s)]");
            scanner = new Scanner(System.in);
            saisieUtilisateur = scanner.nextLine();
            if (jeu.getClass().getName().equals("RecherchePlusMoins")) {
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateur(saisieUtilisateur, "chiffres", jeu.getConfiguration().getTailleCombinaison(), jeu.getConfiguration().getListeValeursPossibles());
            }
            else {
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateur(saisieUtilisateur, "caracteresMastermind", jeu.getConfiguration().getTailleCombinaison(), jeu.getConfiguration().getListeValeursPossibles());
            }
            numeroSaisie++;
        }
        if (jeu.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver, jeu.getNombreEssaisRestants())) {
            System.out.println("Félicitations, vous avez gagné en " + (jeu.getConfiguration().getMaxEssais() - jeu.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
        else {
            System.out.println("Proposition : " + saisieUtilisateur + " -> Réponse : "+ jeu.calculerReponseCombinaison(saisieUtilisateur, combinaisonAtrouver));
        }
    }

    protected void jouerTourModeDefenseur(String derniereCombinaisonProposee, String derniereReponseUtilisateur, Jeu jeu) {
        this.derniereCombinaisonProposee = jeu.proposerCombinaison(derniereCombinaisonProposee,derniereReponseUtilisateur);
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        System.out.println("Voici ma proposition : " + this.derniereCombinaisonProposee);
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            if (this.getClass().getName().equals("RecherchePlusMoins")) {
                System.out.println("Evaluez chaque chiffre de la proposition faite en indiquant (sans les crochets) [=] si le chiffre est correct, [+} si le chiffre est supérieur, [-] si le chiffre est inférieur");
                scanner = new Scanner(System.in);
                saisieUtilisateur = scanner.nextLine();
                this.derniereReponseUtilisateur = saisieUtilisateur;
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateur(saisieUtilisateur, "reponseUtilisateurPlusMoinsDefenseur", jeu.getConfiguration().getTailleCombinaison(), "+-=");
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
                saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateur(saisieUtilisateur, "reponseUtilisateurMastermind", 3, "0123456789");
                numeroSaisie++;
            }
        }
        if (jeu.determinerSiFinJeu(saisieUtilisateur)) {
            System.out.println("J'ai trouvé la réponse en " + (jeu.getConfiguration().getMaxEssais() - jeu.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
    }
}
