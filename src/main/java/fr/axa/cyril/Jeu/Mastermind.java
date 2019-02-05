package fr.axa.cyril.Jeu;
import fr.axa.cyril.Menu.Configuration;

public class Mastermind extends Jeu {

    public Mastermind(Configuration configuration, String listePossibilites) {
        super(configuration, listePossibilites);
    }

    /**
     * Pour la vérification de Mastermind, il y aura 2 parcours à faire :
     *  - 1 fois pour déterminer les couleurs bien placées (et dans ce cas, laisser de côté les couleurs trouvées danns la combinaison
     *  - 1 fois pour regarder chaque couleur restante dans la combinaison de l'utilisateur et vérifier si celle-ci est présente dans la solution, et la laisser de côté le cas échéant
     *  L'objectif est de ne pas revérifier une couleur déjà vérifiée
     * @param saisieUtilisateur : chaine de caractères saisie par l'utilsateur
     * @param combinaisonAtrouver : chaine de caractères générée à trouver
     * @return true si la combinaison a été trouvée, false sinon
     */
    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        int bienPlaces = 0;
        int presents = 0;
        int emplacement;
        if (saisieUtilisateur.equals(combinaisonAtrouver)) {
            System.out.println("Félicitations, vous avez gagné !");
            return true;
        }
        else {
            System.out.println("SAISIE :" + saisieUtilisateur);
            int[] tableauDeVerification = new int[saisieUtilisateur.length()];
            for (int i=0; i<saisieUtilisateur.length(); i++) {
                if (saisieUtilisateur.charAt(i) == combinaisonAtrouver.charAt(i)) {
                    tableauDeVerification[i] = 1;
                    bienPlaces++;
                }
            }
            for (int i=0; i<saisieUtilisateur.length(); i++) {
                emplacement = combinaisonAtrouver.indexOf(saisieUtilisateur.charAt(i));
                if ((emplacement >= 0) && (tableauDeVerification[emplacement] != 1)) {
                    tableauDeVerification[emplacement] = 1;
                    presents++;
                }
            }
            System.out.println("Proposition : "+saisieUtilisateur+" -> Réponse : "+bienPlaces+" bien placé(s), "+presents+" présent(s)");
            return false;
        }
    }
}
