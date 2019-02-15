package fr.axa.cyril.Interface;

import fr.axa.cyril.Controles.ControleSaisie;
import fr.axa.cyril.Jeu.Jeu;
import fr.axa.cyril.Jeu.Mastermind;
import fr.axa.cyril.Jeu.RecherchePlusMoins;
import fr.axa.cyril.Menu.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

/**
 * <b>Interface de jeu est la classe gérant les entrées/sorties de la console</b>
 *     Une classe dédiée permet notamment de faciliter la migration vers un mode autre que console (interface Swing par exemple)
 *     Elle est caractérisée par les informations suivantes :
 *     <ul>
 *         <li>Un scanner qui sera utilisé pour capter la réponse de l'utilisateur tout au long du jeu</li>
 *         <li>La saisie de l'utilisteur</li>
 *         <li>Une information indiquant si le jeu est terminé ou non</li>
 *         <li>La dernière combinaison proposée</li>
 *         <li>La dernière réponse de l'utilisateur</li>
 *         <li>Le jeu choisi par l'utilisateur</li>
 *         <li>Le mode choisi</li>
 *         <li>Le numéro du tour de jeu</li>
 *     </ul>
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class InterfaceJeu {

    private Scanner scanner;
    private String saisieUtilisateur;
    private boolean jeuTermine;
    private String derniereCombinaisonProposee;
    private String derniereReponseUtilisateur;
    private int jeuChoisi;
    private int numeroTour;
    private static Logger logger = LogManager.getLogger(InterfaceJeu.class);

    /**
     * Lancement du jeu
     * Instancie le jeu en question (2 jeux dans chaque mode dans le cas du duel) et joue chaque tour jusqu'à la fin du jeu
     * La fin du jeu arrive si :
     * <ul>
     *     <li>La réponse a été trouvée dans le nombre d'essais impartis</li>
     *     <li>La réponse n'a pas été trouvée et on ne dispose plus d'essai</li>
     * </ul>
     * @param jeuChoisi Le jeu choisi par l'utilisateur
     * @param modeDeJeu Le mode choisi
     * @param configuration La configuration du jeu
     */
    public void lancerJeu(int jeuChoisi, int modeDeJeu, Configuration configuration) {
        logger.trace("Lancement d'une partie de jeu " + jeuChoisi + " en mode " + modeDeJeu);
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
            logger.trace("Fin de  partie de jeu " + jeuChoisi + " en mode " + modeDeJeu);
            if (modeDeJeu == 1) {
                System.out.println("Dommage, vous avez perdu");
                System.out.println("La combinaison à trouver était : " + combinaisonAtrouver);
            } else if (modeDeJeu == 2) {
                System.out.println("Dommage, j'ai perdu");
            }
        }
    }

    /**
     * Un tour de jeu en mode Challenger
     * @param combinaisonAtrouver La combinaison secrète
     * @param jeu l'instance de jeu
     */
    private void jouerTourModeChallenger(String combinaisonAtrouver, Jeu jeu) {
        logger.trace("Début tour de jeu en mode Challenger");
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        System.out.println("------------------------------------------------");
        while (!saisieCorrecte) {
            if (numeroSaisie > 0) {
                System.out.println("ATTENTION : vous avez saisi un caractère ou un nombre de caractères différent de ce qui est attendu.");
            }
            System.out.println("Indiquez votre proposition de "+jeu.getConfiguration().getTailleCombinaison()+" caractères parmi "+ jeu.getConfiguration().getListeValeursPossibles() + " [il vous reste "+jeu.getNombreEssaisRestants()+" essai(s)]");
            scanner = new Scanner(System.in);
            saisieUtilisateur = scanner.nextLine();
            saisieCorrecte = new ControleSaisie().controlerSaisieUtilisateurGenerique(saisieUtilisateur, jeu.getConfiguration().getTailleCombinaison(), jeu.getConfiguration().getListeValeursPossibles());
            numeroSaisie++;
        }
        if (jeu.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver)) {
            System.out.println("Félicitations, vous avez gagné en " + (jeu.getConfiguration().getMaxEssais() - jeu.getNombreEssaisRestants()) + " essai(s)");
            jeuTermine = true;
        }
        else {
            System.out.println("Proposition : " + saisieUtilisateur + " -> Réponse : "+ jeu.calculerReponseCombinaison(saisieUtilisateur, combinaisonAtrouver));
        }
        logger.trace("Fin de tour de jeu en mode Challenger");
    }

    /**
     * Un tour de jeu en mode défenseur
     * @param derniereCombinaisonProposee La dernière combinaison proposée
     * @param derniereReponseUtilisateur La dernière réponse de l'utilisateur
     * @param jeu L'instance de jeu
     */
    private void jouerTourModeDefenseur(String derniereCombinaisonProposee, String derniereReponseUtilisateur, Jeu jeu) {
        logger.trace("Début tour de jeu en mode Defenseur");
        if (this.numeroTour == 0) {
            if (jeuChoisi == 1) {
                System.out.println("--> Veuillez choisir une combinaison de " + jeu.getConfiguration().getTailleCombinaison() + " caractères parmi la liste : " +jeu.getConfiguration().getListeValeursPossibles());
            }
            else {
                System.out.println("--> Veuillez choisir une combinaison de " + jeu.getConfiguration().getTailleCombinaison() + " chiffres parmi la liste : " +jeu.getConfiguration().getListeValeursPossibles());
            }
            System.out.println("Le jeu démarre");
        }
        this.derniereCombinaisonProposee = jeu.proposerCombinaison(derniereCombinaisonProposee,derniereReponseUtilisateur);
        boolean saisieCorrecte = false;
        int numeroSaisie = 0;
        System.out.println("------------------------------------------------");
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
        logger.trace("Fin tour de jeu en mode Defenseur");
    }

    /**
     * Tour de jeu en mode duel
     * Joue à tour de rôle :
     * <ul>
     *     <li>Un tour de jeu en mode challenger</li>
     *     <li>Un tour de jeu en mode défenseur</li>
     * </ul>
     * En cas de victoire, s'arrête après que les 2 tours soient joués afin de gérer les cas d'égalité
     * @param numeroTour Le numéro du tour
     * @param combinaisonAtrouver LA combinaison secrète de l'ordinateur à trouver
     * @param jeuChallenger L'instance de jeu challenger
     * @param derniereCombinaisonProposee La dernière combinaison proposée
     * @param derniereReponseUtilisateur La dernière réponse de l'utilisateur
     * @param jeuDefenseur L'instance de jeu défenseur
     */
    private void jouerTourModeDuel(int numeroTour, String combinaisonAtrouver, Jeu jeuChallenger, String derniereCombinaisonProposee, String derniereReponseUtilisateur, Jeu jeuDefenseur) {
        logger.trace("Début tour de jeu en mode Duel");
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
        logger.trace("Fin tour de jeu en mode Duel");
    }
}