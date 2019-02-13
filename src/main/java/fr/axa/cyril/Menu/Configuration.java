package fr.axa.cyril.Menu;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private String nom;
    private String description;
    private int tailleCombinaison;
    private int maxEssais;
    private boolean modeDebug;
    private String listeValeursPossibles;
    private int nombreCouleurs;

    public Configuration() {
    }

    public void initConfiguration(String cheminFichierProperties, String jeu) {
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
            //this.initUneConfiguration(jeu, proprietes);
            input.close();
        } catch (IOException e) {
            System.out.println("Impossible de charger le fichier " + cheminFichierProperties);
        }
    }

    /**
     * Initialisation de la configuration du jeu Mastermind
     * A noter qu'initialement, la configuration était générique quel que soit le jeu (1 méthode pour tous), mais cela génère des warning "Unused Property"
     * @param proprietes Les properties du fichier config.properties
     */
    private void initConfigurationMastermind(Properties proprietes) {
        try {
            this.nom =  "Mastermind";
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
            this.nom =  "Recherche +/-";
            this.listeValeursPossibles = proprietes.getProperty("recherche.listeValeursPossibles");
            this.tailleCombinaison = Integer.valueOf(proprietes.getProperty("recherche.tailleCombinaison"));
            this.maxEssais = Integer.valueOf(proprietes.getProperty("recherche.maxEssais"));
            this.modeDebug = Boolean.valueOf(proprietes.getProperty("recherche.modeDebug"));
            this.description = proprietes.getProperty("recherche.description");
        } catch (Exception e) {
            System.out.println("Une information nécessaire au lancement du jeu Recherche +/- est manquante ou erronée dans le fichier config.properties");
        }
    }

    /*
    private void initUneConfiguration(String jeu, Properties proprietes) {
        try {
            if (jeu.equals("mastermind"))
            {
                this.nom = "Mastermind";
                this.nombreCouleurs = Integer.valueOf(proprietes.getProperty(jeu + ".nombreCouleurs"));
                this.listeValeursPossibles = proprietes.getProperty(jeu + ".listeValeursPossibles").substring(0,this.nombreCouleurs);
            }
            else {
                this.nom = "Recherche +/-";
                this.listeValeursPossibles = proprietes.getProperty(jeu + ".listeValeursPossibles");
            }
            this.tailleCombinaison = Integer.valueOf(proprietes.getProperty(jeu + ".tailleCombinaison"));
            this.maxEssais = Integer.valueOf(proprietes.getProperty(jeu + ".maxEssais"));
            this.modeDebug = Boolean.valueOf(proprietes.getProperty(jeu + ".modeDebug"));
            this.description = proprietes.getProperty(jeu + ".description");
        } catch (Exception e) {
            System.out.println("Une information nécessaire au lancement du jeu " + jeu + " est manquante ou erronée dans le fichier config.properties");
        }
    }*/

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
