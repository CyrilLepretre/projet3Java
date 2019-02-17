package fr.axa.cyril.Jeu;
import fr.axa.cyril.Menu.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Mastermind est la classe représentant une partie de Mastermind</b>
 *     Elle est compososée d'informations issues de la classe dont elle hérite à savoir :
 *     <ul>
 *         <li>Une configuration</li>
 *         <li>Un nombre d'essais restants</li>
 *         <li>Un statut de fin</li>
 *     </ul>
 *     et d'informations spécifiques :
 *     <ul>
 *         <li>Un tableau de combinaison candidates, qui sera réduit par suppression d'éléments jusqu'à avoir la bonne combinaison</li>
 *         <li>Le score maximum qu'une combinaison peut obtenir, quand elle est parfaitement égale à la combinaison secrète</li>
 *     </ul>
 *     Pour ce jeu, je me suis inspiré de l'algorithme "Five Guess" (les 5 conjonctions) de Knuth, qui semble être le plus performant pour résoudre le problème du Mastermind
 *     Les statistiques de cette algortihme sont disponibles dans ce document https://www.apmep.fr/IMG/pdf/10-Mastermind_C.pdf dont je me suis inspiré pour comprendre son fonctionnement, puis que j'ai implémenté
 *     Dans le cas d'un arangement avec remise (plusieurs fois la même couleur possible), combinaison de 4 couleurs parmi 6 possibles :
 *     <ul>
 *         <li>La combinaison secrète est trouvée en 4 essais dans 20% des cas environ</li>
 *         <li>La combinaison secrète est trouvée en 5 essais dans 80% des cas environ</li>
 *     </ul>
 *
 * @see Jeu
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
    private final int scoreMaximum;

    /**
     * Constructeur du jeu Mastermind
     *
     */
    private static Logger logger = LogManager.getLogger(Mastermind.class);

    public Mastermind(Configuration configuration) {
        super(configuration);
        logger.info("Instanciation d'un objet Mastermind");
        this.scoreMaximum = 10 * this.getConfiguration().getTailleCombinaison();
        logger.info("Fin de l'instanciation de l'objet Mastermind");
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
    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        boolean reponse;
        logger.info("Vérification de combinaison -> chaine saisie : " + saisieUtilisateur + " | combinaison à trouver : " + combinaisonAtrouver);
        this.setNombreEssaisRestants(this.getNombreEssaisRestants()-1);
        int[] tableauScoreObtenu = this.calculerScoreCombinaison(saisieUtilisateur, combinaisonAtrouver);
        reponse = (transformerTableauScoreEnEntier(tableauScoreObtenu) == scoreMaximum);
        logger.debug("Résultat de la vérifification : " + reponse);
        this.setJeuFini(reponse);
        return (reponse);
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
        logger.info("Proposition de combinaison");
        this.setNombreEssaisRestants(this.getNombreEssaisRestants()-1);
        String[] reponseAsplitter;
        int scoreReponse;
        String reponse;
        if (combinaisonEnCours.equals("")) {
            listeCandidats = new ArrayList<>();
            construireListeInitialeRec(this.getConfiguration().getTailleCombinaison(), this.getConfiguration().getNombreCouleurs(), this.getConfiguration().getListeValeursPossibles(), "", listeCandidats);
            reponse = this.fournirCombinaisonDePoidsMinimum(listeCandidats);
            logger.debug("Réponse : " + reponse);
            return reponse;
        }
        else {
            reponseAsplitter = saisieUtilisateur.split("-");
            scoreReponse = Integer.valueOf(reponseAsplitter[0])*10 + Integer.valueOf(reponseAsplitter[1]);
            listeCandidats.removeIf((String valeurCombinaisonCandidat) -> (this.transformerTableauScoreEnEntier(calculerScoreCombinaison(valeurCombinaisonCandidat, combinaisonEnCours)) != scoreReponse));
            //System.out.println("OK, on continue. Il me reste " + listeCandidats.size() + " combinaisons possibles");
            logger.debug("Reste " + listeCandidats.size() + " combinaisons possibles");
            reponse = this.fournirCombinaisonDePoidsMinimum(listeCandidats);
            logger.debug("Combinaison proposée : " + reponse);
            return reponse;
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
        logger.info("Fourniture de combinaison de poids minimum");
        int poidsMinimum=9999;
        int maxPoidsCombinaison;
        String combinaisonDePoidsMinimum = "";
        for (String valeurCombinaison : listeCandidatsRestants) {
            maxPoidsCombinaison = this.calculerMaxPoidsCombinaison(valeurCombinaison, listeCandidatsRestants);
            if (maxPoidsCombinaison < poidsMinimum) {
                poidsMinimum = maxPoidsCombinaison;
                combinaisonDePoidsMinimum = valeurCombinaison;
            }
        }
        //logger.debug("Combinaison proposée : " + combinaisonDePoidsMinimum);
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
        int[] tableauDePoids = new int[10*this.getConfiguration().getTailleCombinaison() + 1];
        for (int i=0; i<=10*this.getConfiguration().getTailleCombinaison(); i++) {
            tableauDePoids[i] = 0;
        }
        for (String valeurCombinaison : listeCandidatsAtt) {
            tableauDePoids[transformerTableauScoreEnEntier(calculerScoreCombinaison(valeurCombinaison, combinaisonEvaluee))] += 1;
        }
        for (int i=0; i<=10*this.getConfiguration().getTailleCombinaison(); i++) {
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
     * @param reponseEvalutionUtilisateur La réponse qu'a fourni l'utilisateur sur le mode défenseur
     * @return true si le jeu est terminé, false sinon
     */
    public boolean determinerSiFinJeu(String reponseEvalutionUtilisateur) {
        String[] reponseASplitter = reponseEvalutionUtilisateur.split("-");
        this.setJeuFini(Integer.valueOf(reponseASplitter[0]) == this.getConfiguration().getTailleCombinaison());
        return this.getJeuFini();
    }

    /**
     * Méthode non utilisée dans le cadre du Mastermind
     * @param saisieUtilisateur Réponse de l'utilisateur à la proposition faite au préalable
     * @return true dans tous les cas
     */
    public boolean verifierErreurFonctionnelle(String saisieUtilisateur) {
        return true;
    }
}