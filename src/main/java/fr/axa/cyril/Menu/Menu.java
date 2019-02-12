package fr.axa.cyril.Menu;

import fr.axa.cyril.Interface.InterfaceMastermind;
import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
import fr.axa.cyril.Interface.InterfaceRecherchePlusMoins;
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
        Scanner saisie = new Scanner(System.in);
        System.out.println("-----------------------------------------------");
        System.out.println("                     MENU                      ");
        System.out.println("-----------------------------------------------");
        System.out.println("Les jeux disponibles : ");
        System.out.println("1 : Mastermind");
        System.out.println("2 : Recherche +/-");
        System.out.println("A quel jeu souhaitez-vous jouer ?");
        int jeuChoisi = saisie.nextInt();
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
        int modeChoisi = saisie.nextInt();
        if (jeuChoisi == 1) {
            InterfaceMastermind interfaceMastermind = new InterfaceMastermind();
            interfaceMastermind.lancerJeu(modeChoisi, mastermindConf);
            this.affichageMenuSuivant(jeuChoisi, modeChoisi);
        }
        else {
            InterfaceRecherchePlusMoins interfaceRecherchePlusMoins = new InterfaceRecherchePlusMoins();
            interfaceRecherchePlusMoins.lancerJeu(modeChoisi, recherchePlusMoinsConf);
            this.affichageMenuSuivant(jeuChoisi, modeChoisi);
        }

        /*switch (modeChoisi) {
            case 1 :
                System.out.println("******** Challenger ********");
                break;
            case 2 :
                System.out.println("******** Défenseur ********");
                // Améliorer avec un substring de getlisteValeursPossibles, et augmenter la liste dans config.properties
                System.out.println("Choisissez une combinaison de " + mastermindConf.getTailleCombinaison() + " couleurs parmi " + mastermindConf.getListeValeursPossibles() + ", vous l'avez ? Retenez-la, le jeu démarre");
                System.out.println();
                break;
            case 3 :
                System.out.println("******** Duel ********");
                break;
        }
        if (jeuChoisi == 1) {
            Mastermind mastermind = new Mastermind(mastermindConf);
            mastermind.demarrer(modeChoisi);
        }
        else {
            RecherchePlusMoins recherchePlusMoins = new RecherchePlusMoins(recherchePlusMoinsConf);
            recherchePlusMoins.demarrer(modeChoisi);
        }*/
    }

    private void affichageMenuSuivant(int jeuPrecedent, int modePrecedent) {
        Scanner saisie = new Scanner(System.in);
        System.out.println("##########################################");
        System.out.println("Que souhaitez-vous faire à présent ?");
        System.out.println("1 : Rejouer au même jeu et même mode");
        System.out.println("2 : Lancer un autre jeu");
        System.out.println("3 : Quitter l'application");
        int choix = saisie.nextInt();
        if (choix == 1) {
            if (jeuPrecedent == 2) {
                InterfaceRecherchePlusMoins interfaceRecherchePlusMoins = new InterfaceRecherchePlusMoins();
                interfaceRecherchePlusMoins.lancerJeu(modePrecedent, recherchePlusMoinsConf);
            }
            else {
                InterfaceMastermind interfaceMastermind = new InterfaceMastermind();
                interfaceMastermind.lancerJeu(modePrecedent, mastermindConf);
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
