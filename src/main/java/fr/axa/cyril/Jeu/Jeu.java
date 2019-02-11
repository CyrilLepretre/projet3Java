package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import java.util.Scanner;

/**
 * Classe abstraite pour un jeu
 * Intègre l'implémentation des méthodes réutilisables par n'importe quel jeu
 * Les méthodes présentant des spécificités en fonction des jeux sont déclarées abstraites, et implémentées dans les classes dédiées
 * @author Cyril Lepretre
 * @version 1.0
 */
public abstract class Jeu {

    /**
     * Configuration du jeu
     */
    protected Configuration configuration;
    //protected String listePossibilites;

    /**
     * Constructeur de jeu
     * @param configuration La configuration issue des paramètres renseignés dans le fichier config.properties
     */
    protected Jeu (Configuration configuration) {
        this.configuration = configuration;
        //this.listePossibilites = listePossibilites;
    }

    /**
     * Génération d'une combinaison au hasard, d'une taille fournie en paramètre, à partir d'une liste renseignée dans le config.properties
     * @param tailleCombinaison La longueur de la combinaison à générer
     * @return La combinaison
     */
    private String genererCombinaison(int tailleCombinaison) {
        StringBuilder combinaison = new StringBuilder(tailleCombinaison);
        for (int i=0; i<tailleCombinaison; i++) {
            combinaison.append(this.configuration.getListeValeursPossibles().charAt(this.genererChiffreRandom(tailleCombinaison)));
        }
        return combinaison.toString();
    }

    /**
     * Génération d'un chiffre aléatoire
     * @param borneSup La borne supérieure limitant la valeur du chiffre généré
     * @return Le chiffre généré alétoirement
     */
    private int genererChiffreRandom (int borneSup) {
        if (borneSup < 1) {
            System.out.println("Erreur de borne supérieure, qui doit être supérieure ou égale à 1");
            return 0;
        }
        else {
            return (int)(Math.random()*borneSup) + 1;
        }
    }

    /**
     * Lancement d'un jeu (Mastermind ou recherche +/-)
     * @param mode : mode de jeu lancé (1=challenger, 2=défenseur, 3=duel)
     */
    public void demarrer(int mode) {
        int nombreEssaisRestants = configuration.getMaxEssais();
        Scanner scanner;
        String saisieUtilisateur;
        String valeursOK;
        String valeursKO;
        boolean jeuFini = false;
        if (mode == 1) {
            String combinaisonAtrouver = this.genererCombinaison(this.configuration.getTailleCombinaison());
            if (this.configuration.getModeDebug()) {
                System.out.println("## MODE DEBUG ===> Combinaison : "+combinaisonAtrouver);
            }
            while ((nombreEssaisRestants > 0)&&(!jeuFini)) {
                nombreEssaisRestants--;
                System.out.println("Indiquez votre proposition de "+configuration.getTailleCombinaison()+" caractères parmi "+ this.configuration.getListeValeursPossibles() + " [il vous reste "+nombreEssaisRestants+" essai(s)]");
                scanner = new Scanner(System.in);
                saisieUtilisateur = scanner.nextLine();
                jeuFini = this.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver, nombreEssaisRestants);
            }
            if (!jeuFini) {
                System.out.println("\n************************Dommage, vous avez perdu ! La réponse était "+combinaisonAtrouver);
            }
        }
        else if (mode == 2) {
            String combinaisonEnCours= "";
            while ((nombreEssaisRestants > 0)&&(!jeuFini)) {
                nombreEssaisRestants--;
                // Code à délocaliser dans les jeux concernés pour pas de code spécifique à un jeu ici
                if (!(combinaisonEnCours.equals(""))) {
                    if (this.getClass().getName().equals("RecherchePlusMoins")) {
                        scanner = new Scanner(System.in);
                        saisieUtilisateur = scanner.nextLine();
                    }
                    else {
                        System.out.println("Combien de couleurs sont à la bonne place : ");
                        scanner = new Scanner(System.in);
                        valeursOK = scanner.nextLine();
                        System.out.println("Combien de couleurs sont à la mauvaise place : ");
                        valeursKO = scanner.nextLine();
                        saisieUtilisateur = valeursOK + "-" + valeursKO;
                    }

                }
                else {
                    saisieUtilisateur = null;
                }
                combinaisonEnCours = this.proposerCombinaison(combinaisonEnCours,saisieUtilisateur);
                System.out.println("Voici ma proposition : " + combinaisonEnCours + "   [" + nombreEssaisRestants + " essai(s) restant(s)]");
            }
        }
    }

    /**
     * Méthode abstraite de vérification de combinaison
     * @param saisieUtilisateur La combinaison fournie par l'utilisateur
     * @param combinaisonAtrouver La combinaison à trouver
     * @param nombreEssaisRestants Le nombre d'essais restants
     * @return true ou false pour indiquer si la combinaison fournie est celle attendue
     */
    abstract boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver, int nombreEssaisRestants);

    /**
     * Méthode abstraite de proposition de combinaison
     * @param combinaisonEnCours La dernière combinaison fournie à l'utilisateur
     * @param saisieUtilisateur La réponse qu'a apporté l'utilisateur suite à la dernière combinaison proposée, qui est l'autre paramètre
     * @return Une nouvelle proposition
     */
    abstract String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur);
}
