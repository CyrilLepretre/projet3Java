package fr.axa.cyril.Controles;

/**
 * <b>ControleSaisie est la classe dédiée aux contrôles des saisies de l'utilisateur</b>
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class ControleSaisie {

    /**
     * Méthode générique de contrôle, qui permet de valider que la saisie de l'utilisateur est conforme aux attentes, passées en paramètres
     * @param saisieUtilisateur La saisie de l'utilisateur
     * @param tailleAttendue La taille de la réponse attendue
     * @param listeValeursValides La liste de caractères valides pour la réponse
     * @return true si la réponse est valide, false sinon
     */
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

    /**
     * Permet de vérifier qu'une réponse fournie au jeu Mastermind en mode défenseur est valide
     * Le cas du Mastermind défenseur est spécifique, car lié à la façon de calculer les score et notamment au fait d'avoir choisi un caractère "-" de split
     * @param saisieUtilisateur La raponse de l'utilisateur
     * @param tailleAttendue La taille de réponse attendue
     * @param listeValeursValides Les caractères autorisés (à l'index 0 et 2 de la réponse, l'index 1 correspondant au caractère "-"
     * @return true si la réponse est valide, false sinon
     */
    public boolean controlerSaisieMastermindDefenseur(String saisieUtilisateur, int tailleAttendue, String listeValeursValides) {
        if (saisieUtilisateur.length() != tailleAttendue) {
            return false;
        }
        else if (!(listeValeursValides.indexOf(saisieUtilisateur.charAt(0)) >= 0)) {
            return false;
        }
        else return (listeValeursValides.indexOf(saisieUtilisateur.charAt(2)) >= 0);
    }
}
