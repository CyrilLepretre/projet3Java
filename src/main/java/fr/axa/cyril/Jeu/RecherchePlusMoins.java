package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;

public class RecherchePlusMoins extends Jeu {

    public RecherchePlusMoins(Configuration configuration, String listePossibilites) {
        super(configuration, listePossibilites);
    }

    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        StringBuilder resultat = new StringBuilder(this.configuration.getTailleCombinaison());
        if (saisieUtilisateur.equals(combinaisonAtrouver)) {
            System.out.println("Vous avez gagné !");
            return true;
        }
        else {
            for (int i=0; i<saisieUtilisateur.length(); i++) {
                if (Character.getNumericValue(saisieUtilisateur.charAt(i)) == Character.getNumericValue(combinaisonAtrouver.charAt(i))) {
                    resultat.append('=');
                }
                else if (Character.getNumericValue(saisieUtilisateur.charAt(i)) > Character.getNumericValue(combinaisonAtrouver.charAt(i))) {
                    resultat.append('-');
                }
                else {
                    resultat.append('+');
                }
            }
            System.out.println("Proposition : "+saisieUtilisateur+" -> Réponse : "+resultat);
            return false;
        }
    }
}
