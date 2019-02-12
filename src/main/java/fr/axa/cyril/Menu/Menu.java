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
        Scanner saisie;
        int jeuChoisi = 0;
        System.out.println("-----------------------------------------------");
        System.out.println("                     MENU                      ");
        System.out.println("-----------------------------------------------");
        System.out.println("Les jeux disponibles : ");
        System.out.println("1 : Mastermind");
        System.out.println("2 : Recherche +/-");
        System.out.println("A quel jeu souhaitez-vous jouer ?");
        boolean saisieCorrecte = false;
        while (!saisieCorrecte) {
            System.out.println("Veuillez saisir 1 ou 2");
            try {
                saisie = new Scanner(System.in);
                jeuChoisi = saisie.nextInt();
            } catch (InputMismatchException exceptionSaisie) {
                System.out.println("Erreur de saisie !");
            }
            if (new ControleSaisie().controlerSaisieUtilisateurGenerique(Integer.toString(jeuChoisi), 1, "12"))
            {
                saisieCorrecte = true;
            }
        }
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
        System.out.println("Les modes disponibles : ");
        System.out.println("1 : Challenger ---------> Trouvez la combinaison secrète de l'ordinateur");
        System.out.println("2 : Défenseur ----------> L'ordinateur doit trouver votre combinaison secrète");
        System.out.println("3 : Duel ---------------> Jouez à tour de rôle contre l'ordinateur");
        System.out.println("Quel mode choisissez-vous ?");
        int modeChoisi = 0;
        saisieCorrecte = false;
        while (!saisieCorrecte) {
            System.out.println("Veuillez saisir 1, 2 ou 3");
            try {
                saisie = new Scanner(System.in);
                modeChoisi = saisie.nextInt();
            } catch (InputMismatchException exceptionSaisie) {
                System.out.println("Erreur de saisie !");
            }
            if (new ControleSaisie().controlerSaisieUtilisateurGenerique(Integer.toString(modeChoisi), 1, "123"))
            {
                saisieCorrecte = true;
            }
        }
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
        Scanner saisie;
        System.out.println("##########################################");
        System.out.println("Que souhaitez-vous faire à présent ?");
        System.out.println("1 : Rejouer au même jeu et même mode");
        System.out.println("2 : Lancer un autre jeu");
        System.out.println("3 : Quitter l'application");
        int choix = 0;
        boolean saisieCorrecte = false;
        while (!saisieCorrecte) {
            System.out.println("Veuillez saisir 1, 2 ou 3");
            try {
                saisie = new Scanner(System.in);
                choix = saisie.nextInt();
            } catch (InputMismatchException exceptionSaisie) {
                System.out.println("Erreur de saisie !");
            }
            if (new ControleSaisie().controlerSaisieUtilisateurGenerique(Integer.toString(choix), 1, "123"))
            {
                saisieCorrecte = true;
            }
        }
        //choix = saisie.nextInt();
        if (choix == 1) {
            if (jeuPrecedent == 2) {
                InterfaceJeu interfaceRecherchePlusMoins = new InterfaceJeu();
                interfaceRecherchePlusMoins.lancerJeu(2, modePrecedent, recherchePlusMoinsConf);
            }
            else {
                InterfaceJeu interfaceMastermind = new InterfaceJeu();
                interfaceMastermind.lancerJeu(1, modePrecedent, mastermindConf);
            }
        }
        else if (choix == 2) {
            this.affichageMenuInitial();
        }
        else {
            System.out.println("A bientôt");
        }
    }


}
