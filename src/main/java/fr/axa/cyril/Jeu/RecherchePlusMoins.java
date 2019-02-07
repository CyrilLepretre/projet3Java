package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;

public class RecherchePlusMoins extends Jeu {
    private int borneInf;
    private int borneSup;

    public RecherchePlusMoins(Configuration configuration) {
        super(configuration);
        borneInf = Character.getNumericValue(this.configuration.getListeValeursPossibles().charAt(0));
        borneSup = Character.getNumericValue(this.configuration.getListeValeursPossibles().charAt(this.configuration.getListeValeursPossibles().length() - 1));
    }

    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver, int nombreEssaisRestants) {
        StringBuilder resultat = new StringBuilder(this.configuration.getTailleCombinaison());
        if (saisieUtilisateur.equals(combinaisonAtrouver)) {
            System.out.println("Félicitations, vous avez gagné en " + nombreEssaisRestants + " essais");
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

    /**
     * On utilise ici la recherche dichotomique.
     * La première proposition est toujours la même (555...). Puis en fonction des réponses de l'utilisateur, on calcule la moyenne
     * entre les bornes supérieure et inférieure, avant de décaler ces mêmes bornes pour le prochain coup
     * @param combinaisonEnCours : combinaison en cours d'identification par l'ordinateur
     * @param saisieUtilisateur : réponse fournie par l'utisateur pour indiquer des +/-/= pour chaque chiffre
     * @return la chaine de chiffres calculée par l'ordinateur
     */
    public String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur) {
        int complement;
        StringBuilder proposition = new StringBuilder(configuration.getTailleCombinaison());
        if (combinaisonEnCours.equals("")) {
            for (int i = 0; i<configuration.getTailleCombinaison(); i++) {
                proposition.append('5');
            }
        }
        else {
            for (int i = 0; i<configuration.getTailleCombinaison(); i++) {
                switch (saisieUtilisateur.charAt(i)) {
                    case '=' :
                        proposition.append(combinaisonEnCours.charAt(i));
                        break;
                    case '-' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) - Math.max(((Character.getNumericValue(combinaisonEnCours.charAt(i)) - borneInf) / 2), 1);
                        borneSup = complement;
                        proposition.append(complement);
                        break;
                    case '+' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) + Math.max(((borneSup - (Character.getNumericValue(combinaisonEnCours.charAt(i)))) / 2), 1);
                        proposition.append(complement);
                        borneInf = complement;
                        break;
                }
            }
        }
        return proposition.toString();
    }
}
