package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;

/**
 * Classe abstraite pour un jeu
 * Intègre l'implémentation des méthodes réutilisables par n'importe quel jeu
 * Les méthodes présentant des spécificités en fonction des jeux sont déclarées abstraites, et implémentées dans les classes dédiées
 * @author Cyril Lepretre
 * @version 1.0
 */
public abstract class Jeu {

    /**
     * Configuration du jeu
     */
    protected Configuration configuration;
    protected int nombreEssaisRestants;
    protected boolean jeuFini;

    /**
     * Constructeur de jeu
     * @param configuration La configuration issue des paramètres renseignés dans le fichier config.properties
     */
    protected Jeu (Configuration configuration) {
        this.configuration = configuration;
        this.jeuFini = false;
        this.nombreEssaisRestants = configuration.getMaxEssais();
    }

    public boolean getJeuFini() {
        return jeuFini;
    }

    public int getNombreEssaisRestants() {
        return nombreEssaisRestants;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Génération d'une combinaison au hasard, d'une taille fournie en paramètre, à partir d'une liste renseignée dans le config.properties
     * @param tailleCombinaison La longueur de la combinaison à générer
     * @return La combinaison
     */
    public String genererCombinaison(int tailleCombinaison) {
        StringBuilder combinaison = new StringBuilder(tailleCombinaison);
        for (int i=0; i<tailleCombinaison; i++) {
            combinaison.append(this.configuration.getListeValeursPossibles().charAt(this.genererChiffreRandom(tailleCombinaison)));
        }
        return combinaison.toString();
    }

    /**
     * Génération d'un chiffre aléatoire
     * @param borneSup La borne supérieure limitant la valeur du chiffre généré
     * @return Le chiffre généré alétoirement
     */
    private int genererChiffreRandom (int borneSup) {
        if (borneSup < 1) {
            System.out.println("Erreur de borne supérieure, qui doit être supérieure ou égale à 1");
            return 0;
        }
        else {
            return (int)(Math.random()*borneSup) + 1;
        }
    }

    /**
     * Méthode abstraite de vérification de combinaison
     * @param saisieUtilisateur La combinaison fournie par l'utilisateur
     * @param combinaisonAtrouver La combinaison à trouver
     * @param nombreEssaisRestants Le nombre d'essais restants
     * @return true ou false pour indiquer si la combinaison fournie est celle attendue
     */
    public abstract boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver, int nombreEssaisRestants);

    /**
     * Méthode abstraite de proposition de combinaison
     * @param combinaisonEnCours La dernière combinaison fournie à l'utilisateur
     * @param saisieUtilisateur La réponse qu'a apporté l'utilisateur suite à la dernière combinaison proposée, qui est l'autre paramètre
     * @return Une nouvelle proposition
     */
    public abstract String proposerCombinaison(String combinaisonEnCours, String saisieUtilisateur);

    /**
     *
     * @param saisieUtilisateur
     * @param combinaisonAtrouver
     * @return
     */
    public abstract String calculerReponseCombinaison(String saisieUtilisateur, String combinaisonAtrouver);

    public abstract boolean determinerSiFinJeu(String reponseEvalutionUtilisateur);

}
