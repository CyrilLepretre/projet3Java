package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import java.util.Scanner;

public abstract class Jeu {

    protected Configuration configuration;
    //protected String listePossibilites;

    protected Jeu (Configuration configuration) {
        this.configuration = configuration;
        //this.listePossibilites = listePossibilites;
    }

    private String genererCombinaison(int tailleCombinaison) {
        StringBuilder combinaison = new StringBuilder(tailleCombinaison);
        for (int i=0; i<tailleCombinaison; i++) {
            combinaison.append(this.configuration.getListeValeursPossibles().charAt(this.genererChiffreRandom(tailleCombinaison)));
        }
        return combinaison.toString();
    }

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

    abstract boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver, int nombreEssaisRestants);

    abstract String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur);
}
