package fr.axa.cyril.Controles;

public class ControleSaisie {

    public boolean controlerSaisieUtilisateurGenerique(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        for (int i = 0; i<tailleAttendue; i++) {
            if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(i)) >= 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean controlerSaisieMastermindDefenseur(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        else if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(0)) >= 0)) {
            return false;
        }
        else return (listeValeursValides.indexOf(saisieUtilisateur.charAt(2)) >= 0);
            /*if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(2)) >= 0)) {
            return false;
        }
        return true;*/
    }

    /*public boolean controlerSaisieRecherchePlusMoinsDefenseur(String saisieUtilisateur, int[] borneSup, int[] borneInf, int maximum, int minimum) {
        for (int i=0; i < saisieUtilisateur.length(); i++) {
            if (((borneSup[i] == minimum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('-'))) || ((borneInf[i] == maximum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('+')))) {
                return false;
            }
        }
        return true;
    }*/
}
