package fr.axa.cyril.Jeu;
import fr.axa.cyril.Menu.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Jeu Mastermind
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class Mastermind extends Jeu {
    /**
     * La liste des candidats pour le mode défensif
     */
    private ArrayList<String> listeCandidats;
    /**
     * Le score maximum qu'un combinaison peut obtenir
     * Sachant qu'un pion bien placé rapporte 10 points, et 1 mal placé 1 point
     */
    private int scoreMaximum;

    /**
     * Constructeur du jeu Mastermind
     * @param configuration La configuration issue des informations fournies dans le config.properties
     */
    public Mastermind(Configuration configuration) {
        super(configuration);
        this.scoreMaximum = 10 * this.configuration.getTailleCombinaison();
    }

    /**
     * Pour la vérification de Mastermind, il y aura 2 parcours à faire :
     * - 1 fois pour déterminer les couleurs bien placées (et dans ce cas, laisser de côté les couleurs trouvées danns la combinaison
     * - 1 fois pour regarder chaque couleur restante dans la combinaison de l'utilisateur et vérifier si celle-ci est présente dans la solution, et la laisser de côté le cas échéant
     * L'objectif est de ne pas revérifier une couleur déjà vérifiée
     *
     * @param saisieUtilisateur La chaine de caractères saisie par l'utilsateur
     * @param combinaisonAtrouver La chaine de caractères générée à trouver
     * @return true si la combinaison a été trouvée, false sinon
     */
    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver, int nombreEssaisRestants) {
        this.nombreEssaisRestants --;
        int[] tableauScoreObtenu = this.calculerScoreCombinaison(saisieUtilisateur, combinaisonAtrouver);
        if (transformerTableauScoreEnEntier(tableauScoreObtenu) == scoreMaximum) {
            return true;
        }
        return false;
    }

    /**
     * Méthode permettant de retourner le score d'une combinaison fournie par l'utilisateur
     * Elle s'appuie sur la méthode calculerReponseCombinaison pour récupérer le tableau des couleurs bien/mal placées
     * @param saisieUtilisateur La combinaison fournie par l'utilisateur
     * @param combinaisonAtrouver La combinaison secrète à trouver
     * @return La réponse sous forme de chaine de caractères qui sera à afficher à l'écran
     */
    public String calculerReponseCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        int[] tableauScoreObtenu = this.calculerScoreCombinaison(saisieUtilisateur, combinaisonAtrouver);
        return (tableauScoreObtenu[0] + " bien placé(s), " + tableauScoreObtenu[1] + " présent(s)");
    }

    /**
     * Nous utilisons ici l'algorithme de Knuth : le "five guess" (cinq conjonctures)
     * Il s'avère être le plus adapté au problème posé dans le Mastermind, avec une solution trouvée en 5 essais maximum pour
     * le jeu classique 4 trous / 6 couleurs
     * Le concept part du fait que parmi toutes les propositions possibles, lorqu'on fait une proposition, et que notre adversaire nous indique le score obtenu (les pions bien placés/présents),
     * la réponse figure forcément parmi les propositions non évaluées, et qui obtienne le même score par rapport à la proposition faite. On fonctionne donc par élimination de celles qui ont un score différent.
     * On commence par initialiser la liste des candidats (ie toutes les possibilités au premier tour) qu'on réduira ensuite à chaque itération
     *
     * Exemple à 3 chiffres : La combinaison secrète est 123. L'algorithme propose 000. Le score est donc de 00 (O bien placés, 0 mal placés)
     *     Par bijection, on peut donc en déduire que la combinaison secrète aura forcément un score de 00 par rapport à la dernière combinaison proposée 000
     *     C'est pourquoi on supprime de la liste des candidats toutes les possibilités dont le score est différent de 00, car elles n'inclueront forcément pas la combinaison secrète
     *
     * @param combinaisonEnCours combinaison de couleurs retournée par l'ordinateur
     * @param saisieUtilisateur réponse de l'utilisateur qui a indiqué les couleurs bien placées en présentes
     * @return nouvelle proposition après application de l'algorithme
     */
    public String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur) {
        String[] reponseAsplitter;
        int scoreReponse;
        if (combinaisonEnCours.equals("")) {
            listeCandidats = new ArrayList<>();
            construireListeInitialeRec(configuration.getTailleCombinaison(), configuration.getNombreCouleurs(), configuration.getListeValeursPossibles(), "", listeCandidats);
            return this.fournirCombinaisonDePoidsMinimum(listeCandidats);
        }
        else {
            reponseAsplitter = saisieUtilisateur.split("-");
            scoreReponse = Integer.valueOf(reponseAsplitter[0])*10 + Integer.valueOf(reponseAsplitter[1]);
            ListIterator<String> parcoursListeCandidats = listeCandidats.listIterator();
            while (parcoursListeCandidats.hasNext()) {
                String valeurCombinaisonCandidat = parcoursListeCandidats.next();
                if (this.transformerTableauScoreEnEntier(calculerScoreCombinaison(valeurCombinaisonCandidat, combinaisonEnCours)) != scoreReponse) {
                    parcoursListeCandidats.remove();
                }
            }
            System.out.println("OK, on continue. Il me reste " + listeCandidats.size() + " combinaisons possibles");
            return this.fournirCombinaisonDePoidsMinimum(listeCandidats);
        }
    }

    /**
     * Méthode récursive de génération de la liste liste initiale de possibilités.
     * Elle permet de générer la liste de possibilités initiale quel que soit la taille des combinaisons
     * @param indexCombinaison taille de la combinaion, décrémentée à chaque appel récursif jusqu'à avoir une taille de 1, qui correspond à l'étape de fin où l'on ajoute une combinaison à la liste
     * @param nombreCouleurs nombre de couleurs utilisables
     * @param listeCouleurs chaine de caractères représentant les couleurs utilisables (1 caractère = 1 couleur)
     * @param debutCombinaison début de combinaison générée par les précédents appels, et dont la taille augmente de 1 à chaque appel récursif, et utilisé lors de l'étape finale
     * @param listeCandidatsRec liste des candidats construits lors des derniers appels
     */
    private void construireListeInitialeRec(int indexCombinaison, int nombreCouleurs, String listeCouleurs, String debutCombinaison, ArrayList<String> listeCandidatsRec) {
        for (int i=0; i<nombreCouleurs; i++) {
            if (indexCombinaison == 1) {
                listeCandidatsRec.add(debutCombinaison + listeCouleurs.charAt(i));
            }
            else {
                construireListeInitialeRec(indexCombinaison - 1, nombreCouleurs, listeCouleurs, debutCombinaison + listeCouleurs.charAt(i), listeCandidatsRec);
            }
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
        int maxPoids = 0;
        // On initialise de 0 à 10 fois la taille de la combinaison nominale, donc taille+1 !
        int[] tableauDePoids = new int[10*this.configuration.getTailleCombinaison() + 1];
        //on initialise le tableau de poids pour la combinaison en cours d'évaluation, la dernière case correspond au score maximum
        for (int i=0; i<=10*this.configuration.getTailleCombinaison(); i++) {
            tableauDePoids[i] = 0;
        }
        //On va calculer le score de chaque possibilité par rapport à la combinaison choisie, et ajouter 1 dans le tableau de poids
        //L'index du tableau correspond au score, et la valeur correspond au poids (ie nombre de combinaison ayant obtenu ce score)
        ListIterator<String> parcoursListePossibilites = listeCandidatsAtt.listIterator();
        while (parcoursListePossibilites.hasNext()) {
            String valeurCombinaison = parcoursListePossibilites.next();
            tableauDePoids[transformerTableauScoreEnEntier(calculerScoreCombinaison(valeurCombinaison, combinaisonEvaluee))] += 1;
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
    private int[] calculerScoreCombinaison (String combinaisonFournie, String combinaisonCible) {
        int bienPlaces = 0;
        int presents = 0;
        int emplacement;
        boolean dejaComptabilise;
        int[] reponse = new int[2];
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
        reponse[0] = bienPlaces;
        reponse[1] = presents;
        return reponse;
    }

    /**
     * Méthode qui renvoie le score à partir d'un tabluea des couleurs bien placées et présentes
     * @param tableauScore tableau de score où l'élément de la colonne 0 correspond aux couleurs bien placées et colonne 1 aux couleurs présentes mal placées
     * @return le score en entier
     */
    private int transformerTableauScoreEnEntier(int[] tableauScore) {
        return 10 * tableauScore[0] + tableauScore[1];
    }

    /**
     * Détermine si le jeu est terminé
     * @param reponseEvalutionUtilisateur
     * @return
     */
    public boolean determinerSiFinJeu(String reponseEvalutionUtilisateur) {
        this.nombreEssaisRestants --;
        String[] reponseASplitter = reponseEvalutionUtilisateur.split("-");
        if (Integer.valueOf(reponseASplitter[0]) == this.configuration.getTailleCombinaison()) {
            jeuFini= true;
        }
        else {
            jeuFini = false;
        }
        return jeuFini;
    }
}