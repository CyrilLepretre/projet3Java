package fr.axa.cyril.Interface;

import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Menu.Configuration;

import java.util.Scanner;

public class InterfaceMastermind extends InterfaceJeu {
    private Scanner scanner;
    private String saisieUtilisateur;
    private boolean jeuTermine;

    public void lancerJeu(int modeDeJeu, Configuration mastermindConf) {
        Mastermind mastermind = new Mastermind(mastermindConf);
        Mastermind mastermind2 = new Mastermind(mastermindConf);
        String combinaisonAtrouver = "";
        if ((modeDeJeu == 1) || (modeDeJeu == 3)) {
            combinaisonAtrouver = mastermind.genererCombinaison(mastermind.getConfiguration().getTailleCombinaison());
            if (mastermindConf.getModeDebug()) {
                System.out.println("## Mode Debug ##    Combinaison à trouver : " + combinaisonAtrouver);
            }
        }
        derniereCombinaisonProposee = "";
        derniereReponseUtilisateur = "";
        jeuTermine = false;
        int numeroTour = 0;
        while ((mastermind.getNombreEssaisRestants() > 0) && (!jeuTermine))//(!(recherchePlusMoins.getJeuFini())) && (!finModeDuel))
        {
            switch (modeDeJeu) {
                case 1 :
                    jouerTourModeChallenger(combinaisonAtrouver, mastermind);
                    break;
                case 2 :
                    jouerTourModeDefenseur(derniereCombinaisonProposee, derniereReponseUtilisateur, mastermind);
                    break;
                /*case 3 :
                    numeroTour++;
                    jouerTourModeDuel(numeroTour, combinaisonAtrouver, mastermind, derniereCombinaisonProposee, derniereReponseUtilisateur, mastermind2);
                    break;*/
            }
        }
        if ((mastermind.getNombreEssaisRestants() == 0) && (!jeuTermine)) {
            System.out.println("Dommage, vous avez perdu");
            if (modeDeJeu == 1) {
                System.out.println("La combinaison à trouver était : " + combinaisonAtrouver);
            }
        }
    }
}
