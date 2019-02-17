package fr.axa.cyril.Menu;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Interface.InterfaceJeu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <b>Menu est la classe représentant le menu du jeu</b>
 *     Elle permet de choisir le jeu parmi :
 *     <ul>
 *         <li>Mastermind</li>
 *         <li>Recherche +/-</li>
 *     </ul>
 *     et le mode de jeu parmi :
 *     <ul>
 *         <li>Challenger : vous devez trouver la combinaison secrète de l'ordinateur</li>
 *         <li>Défenseur : l'ordinateur doit trouver votre combinaison secrète</li>
 *         <li>Duel : vous jouez à tour de rôle contre l'ordinateur et le vainqueur est celui qui trouve la combinaison secrète de l'autre en premier</li>
 *     </ul>
 *     Elle est caractérisée par les configurations de chaque jeu, qu'elle utilise pour afficher des informations concernant le jeu lancé :
 *     <ul>
 *         <li>Configuration du Mastermind</li>
 *         <li>Configuration de Recherche +/-</li>
 *     </ul>
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class Menu {

    private final Configuration mastermindConf;
    private final Configuration recherchePlusMoinsConf;
    private static Logger logger = LogManager.getLogger(Menu.class);
    /**
     * Constructeur du menu
     *
     * Il initialise les configurations des jeux
     */
    public Menu() {
        logger.trace("Lancement du menu");
        mastermindConf = new Configuration();
        recherchePlusMoinsConf = new Configuration();
        try {
            mastermindConf.initConfiguration("/config.properties", "mastermind");
            recherchePlusMoinsConf.initConfiguration("/config.properties", "recherche");
        } catch (Exception e) {
            logger.error("Impossible d'ouvrir le fichier config.properties");
            System.out.println("Impossible d'ouvrir le fichier config.properties");
            System.exit(1);
        }
        logger.info("Fichier config.properties correctement chargé et configurations initialisées");
    }

    /**
     * Affichage du menu initial
     */
    public void affichageMenuInitial() {
        logger.trace("Affichage du menu initial");
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
        System.out.println("\n################################################\n");
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

    /**
     * Affichage du menu après avoir joué à un jeu
     * Propose 3 possibilités :
     * <ul>
     *     <li>Rejouer au même jeu que le précédent</li>
     *     <li>Choisir un autre jeu dans la liste</li>
     *     <li>Quitter</li>
     * </ul>
     * @param jeuPrecedent Le jeu précédemment joué, afin de proposer de rejouer au même jeu que le précédent
     * @param modePrecedent Mode de jeu précédent, pour rejouer dans les mêmes conditions
     */
    private void affichageMenuSuivant(int jeuPrecedent, int modePrecedent) {
        logger.trace("Affichage du menu après avoir terminé une partie");
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
            logger.trace("Sortie de l'application");
        }
    }

    /**
     * Récupération de la réponse de l'utilisateur et contrôle tant qu'elle n'est pas correcte
     * S'appuie sur la classe de contrôle de saisie pour vérifier la saisie de l'utilisateur
     * @see ControleSaisie
     * @param message Ce qu'a saisi l'utilsateur
     * @param valeursAttendues Les caractères acceptés dans la réponse fournie par l'utilisateur
     * @return La réponse valide
     */
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
                logger.error("Mauvaise saisie de l'utilisateur");
                System.out.println("Erreur de saisie !");
            }
            if (new ControleSaisie().controlerSaisieUtilisateurGenerique(Integer.toString(reponse), 1, valeursAttendues))
            {
                saisieCorrecte = true;
            }
        }
        return reponse;
    }

    /**
     * Affichage du bandeau de choix de jeu
     */
    private void affichageBandeauChoixJeu() {
        System.out.println("-----------------------------------------------");
        System.out.println("                     MENU                      ");
        System.out.println("-----------------------------------------------");
        System.out.println("Les jeux disponibles : ");
        System.out.println("  1 : Mastermind");
        System.out.println("  2 : Recherche +/-");
        System.out.println("A quel jeu souhaitez-vous jouer ?");
    }

    /**
     * Affichage du bandeau de choix du mode de jeu
     */
    private void affichageBandeauChoixMode() {
        System.out.println("\n################################################\n");
        System.out.println("Les modes disponibles : ");
        System.out.println("  1 : Challenger ---------> Trouvez la combinaison secrète de l'ordinateur");
        System.out.println("  2 : Défenseur ----------> L'ordinateur doit trouver votre combinaison secrète");
        System.out.println("  3 : Duel ---------------> Jouez à tour de rôle contre l'ordinateur");
        System.out.println("Quel mode choisissez-vous ?");
    }

    /**
     * Affichage du bandeau de choix après avoir jouer à un jeu
     */
    private void affichageBandeauSuivant() {
        System.out.println("\n################################################\n");
        System.out.println("Que souhaitez-vous faire à présent ?");
        System.out.println("1 : Rejouer au même jeu et même mode");
        System.out.println("2 : Lancer un autre jeu");
        System.out.println("3 : Quitter l'application");
    }


}
