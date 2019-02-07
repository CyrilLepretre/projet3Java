package fr.axa.cyril.Menu;

import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
/*import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.util.Iterator;
import java.util.List;*/
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

    public void affichageMenu() {
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
                System.out.println("******** RECHERCHE +/- ********");
                System.out.println(recherchePlusMoinsConf.getDescription());
                break;
        }
        System.out.println("Les modes disponibles : ");
        System.out.println("1 : Challenger ---------> Trouvez la combinaison secrète de l'ordinateur");
        System.out.println("2 : Défenseur ----------> L'ordinateur doit trouver votre combinaison secrète");
        System.out.println("3 : Duel ---------------> Jouez à tour de rôle contre l'ordinateur");
        System.out.println("Quel mode choisissez-vous ?");
        int modeChoisi = saisie.nextInt();
        switch (modeChoisi) {
            case 1 :
                System.out.println("******** Challenger ********");
                break;
            case 2 :
                System.out.println("******** Défenseur ********");
                System.out.println("Choisissez une combinaison, vous l'avez ? Retenez-la, le jeu démarre");
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
        }
    }


}
