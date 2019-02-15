package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>RecherchePlusMoins est la classe représentant une partie du jeu RecherchePlusMoins</b>
 *     Elle est compososée d'informations issues de la classe dont elle hérite à savoir :
 *     <ul>
 *         <li>Une configuration</li>
 *         <li>Un nombre d'essais restants</li>
 *         <li>Un statut de fin</li>
 *     </ul>
 *     et d'informations spécifiques :
 *     <ul>
 *         <li>Une valeur maximum (correspondant au max dans les chiffres qu'on peut renseigner)</li>
 *         <li>Une valeur minimum (correspondant au min dans les chiffres qu'on peut renseigner)</li>
 *         <li>Un tableau de bornes supérieures permettant de réduire le max au fur et à mesure</li>
 *         <li>Un tableau de bornes inférieures permettant d'augementer le min au fur et à mesure</li>
 *     </ul>
 *
 * @see Jeu
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class RecherchePlusMoins extends Jeu {
    private final int maximum;
    private final int minimum;
    private int[] borneInf;
    private int[] borneSup;
    private static Logger logger = LogManager.getLogger(RecherchePlusMoins.class);

    /**
     * Constructeur d'un jeu Recherche +/-
     * Pose les bornes inférieure et supérieure qui seront utilisées pour découvrir la combianison secrète de l'utilisateur
     * Initalilse également le maximum et minimum qui correspondent respectivement à la borne supérieure intiale et borne inférieure initiale
     * @param configuration configuration du Recherche +/-
     */
    public RecherchePlusMoins(Configuration configuration) {
        super(configuration);
        borneInf = new int[this.getConfiguration().getListeValeursPossibles().length()];
        borneSup = new int[this.getConfiguration().getListeValeursPossibles().length()];
        for (int i=0; i < this.getConfiguration().getListeValeursPossibles().length(); i++) {
            borneInf[i] = Character.getNumericValue(this.getConfiguration().getListeValeursPossibles().charAt(0));
            borneSup[i] = Character.getNumericValue(this.getConfiguration().getListeValeursPossibles().charAt(this.getConfiguration().getListeValeursPossibles().length() - 1));
        }
        maximum = borneSup[0];
        minimum = borneInf[0];
    }

    /**
     * Vérifie si la combinaison secrète a été trouvée
     * @param saisieUtilisateur La combinaison fournie par l'utilisateur
     * @param combinaisonAtrouver La combinaison à trouver
     * @return true si la combinaison secrète a été trouvée, false sinon
     */
    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        logger.info("Vérification de combinaison RecherchePlusMoins");
        this.setNombreEssaisRestants(this.getNombreEssaisRestants()-1);
        if (saisieUtilisateur.equals(combinaisonAtrouver)) {
            this.setJeuFini(true);
            logger.debug("Combinaison trouvée => jeu terminé");
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Construit et retourne l'évaluation de la combinaison proposée par l'utilisateur par rapport à la combinaison secrète à trouver
     * Indique
     * <ul>
     *     <li>Des "+" quand le chiffre à trouver est plus grand</li>
     *     <li>Des "-" quand il est plus petit</li>
     *     <li>Des "=" si le chiffre proposé est correct</li>
     * </ul>
     * @param saisieUtilisateur La proposition fournie par l'utilisateur
     * @param combinaisonAtrouver La combinaison secrète à trouver
     * @return La chaîne de caractères correspondant à la réponse à afficher à l'utilisateur
     */
    public String calculerReponseCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        logger.info("Evaluation de la combinaison RecherchePlusMoins - Combinaison saisie : " + saisieUtilisateur + " | combinaison à trouver : " + combinaisonAtrouver);
        StringBuilder resultat = new StringBuilder(this.getConfiguration().getTailleCombinaison());
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
        logger.debug("-> Résultat de l'évaluation : " + resultat.toString());
        return resultat.toString();
    }

    /**
     * On utilise ici la recherche dichotomique.
     * La première proposition est toujours la même (555...). Puis en fonction des réponses de l'utilisateur, on calcule la moyenne
     * entre les bornes supérieure et inférieure, avant de décaler ces mêmes bornes pour le prochain coup
     * @param combinaisonEnCours combinaison en cours d'identification par l'ordinateur
     * @param saisieUtilisateur réponse fournie par l'utisateur pour indiquer des +/-/= pour chaque chiffre
     * @return la chaine de chiffres calculée par l'ordinateur
     */
    public String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur) {
        logger.info("Proposition de combinaison RecherchePlusMoins - dernière combinaison proposée : " + combinaisonEnCours + " | réponse de l'utilisateur : " + saisieUtilisateur);
        this.setNombreEssaisRestants(this.getNombreEssaisRestants()-1);
        int complement;
        StringBuilder proposition = new StringBuilder(this.getConfiguration().getTailleCombinaison());
        if (combinaisonEnCours.equals("")) {
            for (int i = 0; i<this.getConfiguration().getTailleCombinaison(); i++) {
                proposition.append('5');
            }
        }
        else {
            for (int i = 0; i< this.getConfiguration().getTailleCombinaison(); i++) {
                switch (saisieUtilisateur.charAt(i)) {
                    case '=' :
                        proposition.append(combinaisonEnCours.charAt(i));
                        logger.debug("Caractère " + combinaisonEnCours.charAt(i) + " | évalué à = | Caractère proposé : " + combinaisonEnCours.charAt(i));
                        break;
                    case '-' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) - Math.max(((Character.getNumericValue(combinaisonEnCours.charAt(i)) - borneInf[i]) / 2), 1);
                        borneSup[i] = complement;
                        proposition.append(complement);
                        logger.debug("Caractère " + combinaisonEnCours.charAt(i) + " | évalué à - | Caractère proposé : " + complement);
                        break;
                    case '+' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) + Math.max(((borneSup[i] - (Character.getNumericValue(combinaisonEnCours.charAt(i)))) / 2), 1);
                        proposition.append(complement);
                        borneInf[i] = complement;
                        logger.debug("Caractère " + combinaisonEnCours.charAt(i) + " | évalué à + | Caractère proposé : " + complement);
                        break;
                }
            }
        }
        logger.debug("Proposition : " + proposition.toString());
        return proposition.toString();
    }

    /**
     * Méthode pour déterminer si le jeu est terminé
     * @param reponseEvalutionUtilisateur La proposition de l'utilisateur
     * @return true si la combinaison secrète a été trouvée, false sinon
     */
    public boolean determinerSiFinJeu(String reponseEvalutionUtilisateur) {
        this.setJeuFini(true);
        for (int i = 0; i < reponseEvalutionUtilisateur.length(); i++) {
            if (!Character.valueOf(reponseEvalutionUtilisateur.charAt(i)).equals('=')) {
                this.setJeuFini(false);
            }
        }
        return this.getJeuFini();
    }

    /**
     * Permet de vérifier si la réponse fournie par l'utilisateur est cohérente par rapport aux bornes en cours
     * @param saisieUtilisateur true si la réponse est cohérente, false sinon
     * @return true si la réponse est cohérente, false sinon (ie la réponse amènerait à des propositions au delà des valeurs autorisées)
     */
    public boolean verifierErreurFonctionnelle(String saisieUtilisateur) {
        logger.info("Vérification erreur fonctionnelle RecherchePlusMoins");
        for (int i=0; i < saisieUtilisateur.length(); i++) {
            if (((borneSup[i] == minimum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('-'))) || ((borneInf[i] == maximum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('+')))) {
                logger.error("Erreur : borne sup = " + borneSup[i] + " | borne inf = " + borneInf[i] + " | utilisateur a saisie : " + saisieUtilisateur);
                return false;
            }
        }
        return true;
    }
}
