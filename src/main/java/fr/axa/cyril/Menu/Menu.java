package fr.axa.cyril.Menu;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Interface.InterfaceJeu;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Menu {

    private Configuration mastermindConf;
    private Configuration recherchePlusMoinsConf;

    public Menu() {
        System.out.println(new File("").getAbsolutePath());
        mastermindConf = new Configuration();
        mastermindConf.initConfiguration("/config.properties" , "mastermind");
        recherchePlusMoinsConf = new Configuration();
        recherchePlusMoinsConf.initConfiguration("/config.properties" , "recherche");
    }

    public void affichageMenuInitial() {
        affichageBandeauChoixJeu();
        int jeuChoisi = saisieMenu12ou123("Veuillez saisir 1 ou 2", "12");
        switch (jeuChoisi) {
            case 1 :
                System.out.println(mastermindConf.getNom());
                System.out.println(mastermindConf.getDescription());
                break;
            case 2 :
                System.out.println(recherchePlusMoinsConf.getNom());
                System.out.println(recherchePlusMoinsConf.getDescription());
                break;
        }
        affichageBandeauChoixMode();
        int modeChoisi = saisieMenu12ou123("Veuillez saisir 1, 2 ou 3", "123");
        if (jeuChoisi == 1) {
            InterfaceJeu interfaceMastermind = new InterfaceJeu();
            interfaceMastermind.lancerJeu(jeuChoisi, modeChoisi, mastermindConf);
            this.affichageMenuSuivant(jeuChoisi, modeChoisi);
        }
        else {
            InterfaceJeu interfaceRecherchePlusMoins = new InterfaceJeu();
            interfaceRecherchePlusMoins.lancerJeu(jeuChoisi, modeChoisi, recherchePlusMoinsConf);
            this.affichageMenuSuivant(jeuChoisi, modeChoisi);
        }
    }

    private void affichageMenuSuivant(int jeuPrecedent, int modePrecedent) {
        affichageBandeauSuivant();
        int choix = saisieMenu12ou123("Veuillez saisir 1, 2 ou 3", "123");
        if (choix == 1) {
            if (jeuPrecedent == 2) {
                InterfaceJeu interfaceRecherchePlusMoins = new InterfaceJeu();
                interfaceRecherchePlusMoins.lancerJeu(2, modePrecedent, recherchePlusMoinsConf);
                this.affichageMenuSuivant(2, modePrecedent);
            }
            else {
                InterfaceJeu interfaceMastermind = new InterfaceJeu();
                interfaceMastermind.lancerJeu(1, modePrecedent, mastermindConf);
                this.affichageMenuSuivant(1, modePrecedent);
            }
        }
        else if (choix == 2) {
            this.affichageMenuInitial();
        }
        else {
            System.out.println("A bientôt");
        }
    }

    private int saisieMenu12ou123(String message, String valeursAttendues) {
        Scanner saisie;
        int reponse=0;
        boolean saisieCorrecte = false;
        while (!saisieCorrecte) {
            System.out.println(message);
            try {
                saisie = new Scanner(System.in);
                reponse = saisie.nextInt();
            } catch (InputMismatchException exceptionSaisie) {
                System.out.println("Erreur de saisie !");
            }
            if (new ControleSaisie().controlerSaisieUtilisateurGenerique(Integer.toString(reponse), 1, valeursAttendues))
            {
                saisieCorrecte = true;
            }
        }
        return reponse;
    }

    private void affichageBandeauChoixJeu() {
        System.out.println("-----------------------------------------------");
        System.out.println("                     MENU                      ");
        System.out.println("-----------------------------------------------");
        System.out.println("Les jeux disponibles : ");
        System.out.println("1 : Mastermind");
        System.out.println("2 : Recherche +/-");
        System.out.println("A quel jeu souhaitez-vous jouer ?");
    }

    private void affichageBandeauChoixMode() {
        System.out.println("Les modes disponibles : ");
        System.out.println("1 : Challenger ---------> Trouvez la combinaison secrète de l'ordinateur");
        System.out.println("2 : Défenseur ----------> L'ordinateur doit trouver votre combinaison secrète");
        System.out.println("3 : Duel ---------------> Jouez à tour de rôle contre l'ordinateur");
        System.out.println("Quel mode choisissez-vous ?");
    }

    private void affichageBandeauSuivant() {
        System.out.println("##########################################");
        System.out.println("Que souhaitez-vous faire à présent ?");
        System.out.println("1 : Rejouer au même jeu et même mode");
        System.out.println("2 : Lancer un autre jeu");
        System.out.println("3 : Quitter l'application");
    }


}
