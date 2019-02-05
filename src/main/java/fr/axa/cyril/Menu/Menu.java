package fr.axa.cyril.Menu;

import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Configuration mastermindConf;
    private Configuration recherchePlusMoinsConf;
    private int jeuChoisi;
    private int modeChoisi;
    private Scanner saisie;
    //private boolean affichageMenu;

    public void initConfiguration() {
        //affichageMenu = true;
        saisie = new Scanner(System.in);
        Document document = new Document();
        SAXBuilder sxb = new SAXBuilder();
        try {
            document = sxb.build(new File("config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element racine;
        racine = document.getRootElement();
        List gamesList = racine.getChildren("game");
        Iterator iteXml = gamesList.iterator();
        while (iteXml.hasNext()) {
            Element actualElement = (Element)iteXml.next();
            String nomXml = actualElement.getChild("nom").getText();
            String descriptionXml = actualElement.getChild("description").getText();
            int tailleCombinaisonXml = Integer.parseInt(actualElement.getChild("tailleCombinaison").getText());
            int maxEssaisXml = Integer.parseInt(actualElement.getChild("maxEssais").getText());
            boolean modeDebugXml = Boolean.parseBoolean(actualElement.getChild("modeDebug").getText());
            if (actualElement.getChild("nom").getText().equals("Mastermind")) {
                int nombreCouleursXml = Integer.parseInt(actualElement.getChild("nombreCouleurs").getText());
                mastermindConf = new Configuration(nomXml,descriptionXml,tailleCombinaisonXml,maxEssaisXml,modeDebugXml,nombreCouleursXml);
            }
            else {
                recherchePlusMoinsConf = new Configuration(nomXml,descriptionXml,tailleCombinaisonXml,maxEssaisXml,modeDebugXml,-1);
            }
        }
    }

    public void affichageMenu() {
        System.out.println("-----------------------------------------------");
        System.out.println("                     MENU                      ");
        System.out.println("-----------------------------------------------");
        System.out.println("Les jeux disponibles : ");
        System.out.println("1 : Mastermind");
        System.out.println("2 : Recherche +/-");
        System.out.println("A quel jeu souhaitez-vous jouer ?");
        jeuChoisi = saisie.nextInt();
        switch (jeuChoisi) {
            case 1 :
                System.out.println("******** MASTERMIND ********");
                break;
            case 2 :
                System.out.println("******** RECHERCHE +/- ********");
                break;
        }
        System.out.println("Les modes disponibles : ");
        System.out.println("1 : Challenger ---------> Trouvez la combinaison secrète de l'ordinateur");
        System.out.println("2 : Défenseur ----------> L'ordinateur doit trouver votre combinaison secrète");
        System.out.println("3 : Duel ---------------> Jouez à tour de rôle contre l'ordinateur");
        System.out.println("Quel mode choisissez-vous ?");
        modeChoisi = saisie.nextInt();
        switch (modeChoisi) {
            case 1 :
                System.out.println("******** Challenger ********");
                break;
            case 2 :
                System.out.println("******** Défenseur ********");
                break;
            case 3 :
                System.out.println("******** Duel ********");
                break;
        }
        if (jeuChoisi == 1) {
            Mastermind mastermind = new Mastermind(mastermindConf, "RVBJMNGCOP");
            mastermind.demarrer(modeChoisi);
        }
        else {
            RecherchePlusMoins recherchePlusMoins = new RecherchePlusMoins(recherchePlusMoinsConf, "0123456789");
            recherchePlusMoins.demarrer(modeChoisi);
        }
    }


}
