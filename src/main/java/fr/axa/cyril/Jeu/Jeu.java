package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;

import java.util.Scanner;

public abstract class Jeu {

    protected Configuration configuration;
    protected String listePossibilites;

    public Jeu (Configuration configuration, String listePossibilites) {
        this.configuration = configuration;
        this.listePossibilites = listePossibilites;
    }

    public String genererCombinaison(int tailleCombinaison, String listePossibilites) {
        String combinaison = "";
        for (int i=0; i<tailleCombinaison; i++) {
            combinaison += listePossibilites.charAt(this.genererChiffreRandom(tailleCombinaison));
        }
        return combinaison;
    }

    public int genererChiffreRandom (int borneSup) {
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
        boolean jeuFini = false;
        if (mode == 1) {
            String combinaisonAtrouver = this.genererCombinaison(this.configuration.getTailleCombinaison(), listePossibilites);
            if (this.configuration.getModeDebug()) {
                System.out.println("## MODE DEBUG ===> Combinaison : "+combinaisonAtrouver);
            }
            while ((nombreEssaisRestants > 0)&&(!jeuFini)) {
                System.out.println("Indiquez votre proposition de "+configuration.getTailleCombinaison()+" caractères parmi "+listePossibilites + " [il vous reste "+nombreEssaisRestants+" essai(s)]");
                scanner = new Scanner(System.in);
                saisieUtilisateur = scanner.nextLine();
                jeuFini = this.verifierCombinaison(saisieUtilisateur, combinaisonAtrouver);
                nombreEssaisRestants--;
            }
            if (!jeuFini) {
                System.out.println("\n************************Dommage, vous avez perdu ! La réponse était "+combinaisonAtrouver);
            }
        }
        else if (mode == 2) {
            String combinaisonEnCours= "";
            while ((nombreEssaisRestants > 0)&&(!jeuFini)) {
                if (!(combinaisonEnCours.equals(""))) {
                    scanner = new Scanner(System.in);
                    saisieUtilisateur = scanner.nextLine();
                }
                else {
                    saisieUtilisateur = null;
                }
                combinaisonEnCours = this.proposerCombinaison(combinaisonEnCours,saisieUtilisateur);
                nombreEssaisRestants--;
                System.out.println("Voici ma proposition : " + combinaisonEnCours + "   [" + nombreEssaisRestants + " essai(s) restant(s)]");
            }
        }
    }

    abstract boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver);

    abstract String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur);
}
