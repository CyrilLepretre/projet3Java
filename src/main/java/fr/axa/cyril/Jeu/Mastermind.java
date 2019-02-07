package fr.axa.cyril.Jeu;
import fr.axa.cyril.Menu.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Mastermind extends Jeu {

    //private List<String> toutesLesPossibilites;
    private List<String> listeCandidats;

    public Mastermind(Configuration configuration, String listePossibilites) {
        super(configuration, listePossibilites);
    }

    /**
     * Pour la vérification de Mastermind, il y aura 2 parcours à faire :
     * - 1 fois pour déterminer les couleurs bien placées (et dans ce cas, laisser de côté les couleurs trouvées danns la combinaison
     * - 1 fois pour regarder chaque couleur restante dans la combinaison de l'utilisateur et vérifier si celle-ci est présente dans la solution, et la laisser de côté le cas échéant
     * L'objectif est de ne pas revérifier une couleur déjà vérifiée
     *
     * @param saisieUtilisateur   : chaine de caractères saisie par l'utilsateur
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
        } else {
            System.out.println("SAISIE :" + saisieUtilisateur);
            int[] tableauDeVerification = new int[saisieUtilisateur.length()];
            for (int i = 0; i < saisieUtilisateur.length(); i++) {
                if (saisieUtilisateur.charAt(i) == combinaisonAtrouver.charAt(i)) {
                    tableauDeVerification[i] = 1;
                    bienPlaces++;
                }
            }
            for (int i = 0; i < saisieUtilisateur.length(); i++) {
                emplacement = combinaisonAtrouver.indexOf(saisieUtilisateur.charAt(i));
                if ((emplacement >= 0) && (tableauDeVerification[emplacement] != 1)) {
                    tableauDeVerification[emplacement] = 1;
                    presents++;
                }
            }
            System.out.println("Proposition : " + saisieUtilisateur + " -> Réponse : " + bienPlaces + " bien placé(s), " + presents + " présent(s)");
            return false;
        }
    }

    /**
     * Nous utilisons ici l'algorithme de Knuth : le "five guess" (cinq conjonctures)
     * Il s'avère être le plus adapté au problème posé dans le Mastermind, avec une solution trouvée en 5 essais maximum pour
     * le jeu classique 4 trous / 6 couleurs
     * @param combinaisonEnCours
     * @param saisieUtilisateur
     * @return
     */
    public String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur) {
        StringBuilder unePossibilite;// = new StringBuilder(this.configuration.getTailleCombinaison());
        String[] reponseAsplitter;
        int scoreReponse;
        if (combinaisonEnCours.equals("")) {
            //On va initialiser toutesPossibilites lors du premier tour de recherche
            //Au départ, la liste des candidats est toute la liste des possibilités, qu'on réduira ensuite à chaque itération
            //toutesLesPossibilites = new ArrayList<String>();
            listeCandidats = new ArrayList<String>();
            for (int i = 0; i < configuration.getNombreCouleurs(); i++) {
                for (int j = 0; j < configuration.getNombreCouleurs(); j++) {
                    for (int k = 0; k < configuration.getNombreCouleurs(); k++) {
                        for (int m = 0; m < configuration.getNombreCouleurs(); m++) {
                            unePossibilite = new StringBuilder(this.configuration.getTailleCombinaison());
                            unePossibilite.append(listePossibilites.charAt(i));
                            unePossibilite.append(listePossibilites.charAt(j));
                            unePossibilite.append(listePossibilites.charAt(k));
                            unePossibilite.append(listePossibilites.charAt(m));
                            //toutesLesPossibilites.add(unePossibilite.toString());
                            listeCandidats.add(unePossibilite.toString());
                            unePossibilite.setLength(0);
                        }
                    }
                }
            }
            //return toutesLesPossibilites.get((int)(Math.random()*toutesLesPossibilites.size()));
            return this.fournirCombinaisonDePoidsMinimum(listeCandidats);
        }
        else {
            reponseAsplitter = saisieUtilisateur.split("-");
            scoreReponse = Integer.valueOf(reponseAsplitter[0])*10 + Integer.valueOf(reponseAsplitter[1]);
            //System.out.println("Donc le score de cette combinaison est " + scoreReponse + ", OK, on continue");
            // On parcourt la liste des candidats pour supprimer ceux dont le score est différent de celui de la réponse proposée précédemment
            ListIterator<String> parcoursListeCandidats = listeCandidats.listIterator();
            while (parcoursListeCandidats.hasNext()) {
                String valeurCombinaisonCandidat = parcoursListeCandidats.next();
                if (this.calculerScoreCombinaison(valeurCombinaisonCandidat, combinaisonEnCours) != scoreReponse) {
                    // Là, on supprime la proposition de la liste des candidats
                    parcoursListeCandidats.remove();
                }
            }
            System.out.println("OK, on continue. Il me reste " + listeCandidats.size() + " combinaisons possibles");
            // Puis on recommence le processus
            return this.fournirCombinaisonDePoidsMinimum(listeCandidats);
        }
    }

    /**
     * Cette méthode a pour seul but de déterminer parmi toutes les combinaisons candidates restantes celle qui permettra d'éliminer un maximum de possibilités
     * Elle vise exclusivement à optimiser le délai d'identification du bon résultat, par rapport à de simples random
     * Elle parcourt la liste des candidats restants et calcule le poids maximum pour chacun d'entre eux
     * A chaque cycle, elle garde en mémoire celle ayant le poids maximum et retourne in fine la première combinaison ayant le poids maximum identifié
     * @param listeCandidatsRestants liste de String représentant la liste des candidats restants
     * @return combinaison de poids maximum qui sera proposé à l'utilisateur
     */
    private String fournirCombinaisonDePoidsMinimum(List<String> listeCandidatsRestants) {
        int poidsMinimum=9999;
        int maxPoidsCombinaison;
        String combinaisonDePoidsMinimum = "";
        ListIterator<String> parcoursListePossibilites = listeCandidatsRestants.listIterator();
        while (parcoursListePossibilites.hasNext()) {
            String valeurCombinaison = parcoursListePossibilites.next();
            maxPoidsCombinaison = this.calculerMaxPoidsCombinaison(valeurCombinaison, listeCandidatsRestants);
            if (maxPoidsCombinaison < poidsMinimum) {
                poidsMinimum = maxPoidsCombinaison;
                combinaisonDePoidsMinimum = valeurCombinaison;
            }
        }
        return combinaisonDePoidsMinimum;
    }

    /**
     * Calcule le poids maximum d'une combinaison fournie en entrée par rapport à une liste de combinaisons
     * Le poids pour un score donné correspond à la quantité des autres combinaisons de la liste ayant ce score suite à comparaison avec la combinaison en entrée
     * Le poids maximum correspond au maximum de combinaisons de la liste ayant eu un score donné suite à la comporaison
     * @param combinaisonEvaluee Combinaison à évaluer en terme de poids
     * @param listeCandidatsAtt Liste des candidats restants
     * @return Le poids maximum qui a été calculé, donc le nombre maximum de combinaisons de la liste ayant un score X par rapport à la combinaison fournie en entrée
     */
    private int calculerMaxPoidsCombinaison (String combinaisonEvaluee, List<String> listeCandidatsAtt) {
        int poids = 0;
        int maxPoids = 0;
        // On initialise de 0 à 10 fois la taille de la combinaison nominale, donc taille+1 !
        int tableauDePoids[] = new int[10*this.configuration.getTailleCombinaison() + 1];
        //on initialise le tableau de poids pour la combinaison en cours d'évaluation, la dernière case correspond au score maximum
        for (int i=0; i<=10*this.configuration.getTailleCombinaison(); i++) {
            tableauDePoids[i] = 0;
        }
        //On va calculer le score de chaque possibilité par rapport à la combinaison choisie, et ajouter 1 dans le tableau de poids
        //L'index du tableau correspond au score, et la valeur correspond au poids (ie nombre de combinaison ayant obtenu ce score)
        ListIterator<String> parcoursListePossibilites = listeCandidatsAtt.listIterator();
        while (parcoursListePossibilites.hasNext()) {
            String valeurCombinaison = parcoursListePossibilites.next();
            tableauDePoids[calculerScoreCombinaison(valeurCombinaison, combinaisonEvaluee)] += 1;
        }
        //Puis on parcourt le tableau pour identifier le poids maximum obtenu sur un des scores
        for (int i=0; i<=10*this.configuration.getTailleCombinaison(); i++) {
            if (maxPoids < tableauDePoids[i]) {
                maxPoids = tableauDePoids[i];
            }
        }
        return maxPoids;
    }

    /**
     * La méthode calculerScoreCombinaison compare 2 combinaisons et détermine le score de la combinaison fournie
     * Toute couleur bien placée génère 10 points, et toute couleur présente 1 point
     *
     * @param combinaisonFournie combinaison à scorer
     * @param combinaisonCible combinaison de référence par rapport à laquelle la combinaison fournie sera scorée
     * @return le score calculé
     */
    private int calculerScoreCombinaison (String combinaisonFournie, String combinaisonCible) {
        int bienPlaces = 0;
        int presents = 0;
        int emplacement;
        boolean dejaComptabilise;
        int[] tableauDeVerificationFournie = new int[combinaisonFournie.length()];
        int[] tableauDeVerificationCible = new int[combinaisonFournie.length()];
        for (int i = 0; i < combinaisonFournie.length(); i++) {
            if (combinaisonFournie.charAt(i) == combinaisonCible.charAt(i)) {
                tableauDeVerificationFournie[i] = 1;
                tableauDeVerificationCible[i] = 1;
                bienPlaces++;
            }
        }
        for (int i = 0; i < combinaisonFournie.length(); i++) {
            emplacement = combinaisonCible.indexOf(combinaisonFournie.charAt(i));
            dejaComptabilise = false;
            while (emplacement >= 0) {
                if ((!dejaComptabilise) && (tableauDeVerificationCible[emplacement] != 1) && (tableauDeVerificationFournie[i] != 1)) {
                    tableauDeVerificationCible[emplacement] = 1;
                    presents++;
                    dejaComptabilise = true;
                }
                emplacement = combinaisonCible.indexOf(combinaisonFournie.charAt(i), emplacement + 1);
            }

        }
        return 10 * bienPlaces + presents;
    }
}