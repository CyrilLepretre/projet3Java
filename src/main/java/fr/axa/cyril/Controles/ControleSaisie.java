package fr.axa.cyril.Controles;

public class ControleSaisie {

    public boolean controlerSaisieUtilisateur(String saisieUtilisateur, String typeDeSaisieAttendue, int tailleAttendue, String listeValeursValides) {
        if (typeDeSaisieAttendue.equals("chiffres")) {
            return controlerSaisieUtilisateurChiffres(saisieUtilisateur, tailleAttendue, listeValeursValides);
        }
        else if (typeDeSaisieAttendue.equals("menuJeu")) {
            return controlerSaisieUtilisateurMenuJeu(saisieUtilisateur, tailleAttendue, listeValeursValides);
        }
        else if (typeDeSaisieAttendue.equals("reponseUtilisateurPlusMoinsDefenseur")) {
            return controlerSaisieUtilisateurPlusMoinsDefenseur(saisieUtilisateur, tailleAttendue, listeValeursValides);
        }
        else if (typeDeSaisieAttendue.equals("caracteresMastermind")) {
            return controlerSaisieUtilisateurPropositionMastermind(saisieUtilisateur, tailleAttendue, listeValeursValides);
        }
        else if (typeDeSaisieAttendue.equals("reponseUtilisateurMastermind")) {
            return controlerSaisieUtilisateurReponseMastermind(saisieUtilisateur, tailleAttendue, listeValeursValides);
        }
        return true;
    }

    private boolean controlerSaisieUtilisateurReponseMastermind(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        else if ((!Character.isDigit(saisieUtilisateur.charAt(0))) || (!Character.isDigit(saisieUtilisateur.charAt(2)))) {
            return false;
        }
        return true;
    }

    private boolean controlerSaisieUtilisateurPropositionMastermind(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        else if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(0)) >= 0)) {
            return false;
        }
        return true;
    }

    public boolean controlerSaisieUtilisateurPlusMoinsDefenseur(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        else if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(0)) >= 0)) {
            return false;
        }
        return true;
    }

    public boolean controlerSaisieUtilisateurMenuJeu(String saisieUtilsateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilsateur.length() != tailleAttendue) {
            return false;
        }
        else if (!Character.isDigit(saisieUtilsateur.charAt(0))) {
            return false;
        }
        else if (!(listeValeursValides.indexOf(saisieUtilsateur.charAt(0)) >= 0)) {
            return false;
        }
        return true;
    }


    public boolean controlerSaisieUtilisateurChiffres (String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        for (int i = 0; i<tailleAttendue; i++) {
            if (!Character.isDigit(saisieUtilisateur.charAt(i))) {
                return false;
            }
            else if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(i)) >= 0)) {
                return false;
            }
        }
        return true;
    }
}
