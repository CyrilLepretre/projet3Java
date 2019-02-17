package fr.axa.cyril.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <b>Configuration est la classe représentant la configuration d'un jeu.</b>
 *     Une configuration est caractérisée par les informations suivantes :
 *     <ul>
 *         <li>Un nom de jeu</li>
 *         <li>Une description du but du jeu</li>
 *         <li>La taille d'une combinaison</li>
 *         <li>Le maximum d'essais possibles pour le dit jeu</li>
 *         <li>Un indicateur pour déterminer si on active ou non le mode debug</li>
 *         <li>Les listes de valeurs possibles et admissibles dans une proposition</li>
 *         <li>Dans le cas du Mastermind, le nombre de couleurs disponibles</li>
 *     </ul>
 *     La configuration se base sur un fichier config.properties, dans lequel on renseigne l'ensemble des paramètres des jeux
 *
 * @author Cyril Lepretre
 * @version 1.0
 */
public class Configuration {
    private String nom;
    private String description;
    private int tailleCombinaison;
    private int maxEssais;
    private boolean modeDebug;
    private String listeValeursPossibles;
    private int nombreCouleurs;
    private static Logger logger = LogManager.getLogger(Configuration.class);

    public Configuration() {
    }

    /**
     * Constructeur d'une configuration, qui s'appuie sur le fichier fourni en paramètre pour valoriser les variables de classe
     * Compte-tenu des spécificités à chaque jeu, on a 1 objet de type Configuration par jeu
     * @param cheminFichierProperties Le chemin d'accès du fichier config.properties
     * @param jeu Le nom du jeu dont la configuration est instanciée (
     */
    public void initConfiguration(String cheminFichierProperties, String jeu) {
        logger.trace("Début initialisation de la configuration du jeu " + jeu);
        InputStream input;
        Properties proprietes = new Properties();
        try {
            input = Configuration.class.getResourceAsStream(cheminFichierProperties);
            proprietes.load(input);
            if (jeu.equals("mastermind"))
            {
                this.initConfigurationMastermind(proprietes);
            }
            else {
                this.initConfigurationRecherchePlusMoins(proprietes);
            }
            input.close();
        } catch (IOException e) {
            logger.error("Erreur à l'initialisation de la configuration du jeu " + jeu);
            System.out.println("Impossible de charger le fichier " + cheminFichierProperties);
        }
        logger.trace("Fin initialisation de la configuration du jeu " + jeu);
    }

    /**
     * Initialisation de la configuration du jeu Mastermind
     * A noter qu'initialement, la configuration était générique quel que soit le jeu (1 méthode pour tous), mais cela génère des warning "Unused Property"
     * @param proprietes Les properties du fichier config.properties
     */
    private void initConfigurationMastermind(Properties proprietes) {
        try {
            this.nom =  "**** MASTERMIND ****";
            this.nombreCouleurs = Integer.valueOf(proprietes.getProperty("mastermind.nombreCouleurs"));
            this.listeValeursPossibles = proprietes.getProperty("mastermind.listeValeursPossibles").substring(0,this.nombreCouleurs);
            this.tailleCombinaison = Integer.valueOf(proprietes.getProperty("mastermind.tailleCombinaison"));
            this.maxEssais = Integer.valueOf(proprietes.getProperty("mastermind.maxEssais"));
            this.modeDebug = Boolean.valueOf(proprietes.getProperty("mastermind.modeDebug"));
            this.description = proprietes.getProperty("mastermind.description");
        } catch (Exception e) {
            System.out.println("Une information nécessaire au lancement du jeu Mastermind est manquante ou erronée dans le fichier config.properties");
        }
    }

    /**
     * Initialisation de la configuration du jeu Recherche +/-
     * A noter qu'initialement, la configuration était générique quel que soit le jeu (1 méthode pour tous), mais cela génère des warning "Unused Property"
     * @param proprietes Les propriétés du fichier config.properties
     */
    private void initConfigurationRecherchePlusMoins(Properties proprietes) {
        try {
            this.nom =  "**** RECHERCHE +/- ****";
            this.listeValeursPossibles = proprietes.getProperty("recherche.listeValeursPossibles");
            this.tailleCombinaison = Integer.valueOf(proprietes.getProperty("recherche.tailleCombinaison"));
            this.maxEssais = Integer.valueOf(proprietes.getProperty("recherche.maxEssais"));
            this.modeDebug = Boolean.valueOf(proprietes.getProperty("recherche.modeDebug"));
            this.description = proprietes.getProperty("recherche.description");
        } catch (Exception e) {
            System.out.println("Une information nécessaire au lancement du jeu Recherche +/- est manquante ou erronée dans le fichier config.properties");
        }
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getTailleCombinaison() {
        return tailleCombinaison;
    }

    public String getListeValeursPossibles() {
        return listeValeursPossibles;
    }

    public int getMaxEssais() {
        return maxEssais;
    }

    public boolean getModeDebug() {
        return modeDebug;
    }

    public int getNombreCouleurs() {
        return nombreCouleurs;
    }
}
