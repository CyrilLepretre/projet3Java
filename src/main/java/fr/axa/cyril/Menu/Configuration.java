package fr.axa.cyril.Menu;

public class Configuration {
    private String nom;
    private String description;
    private int tailleCombinaison;
    private int maxEssais;
    private boolean modeDebug;
    private int nombreCouleurs;

    public Configuration() {

    }

    public Configuration(String nom, String description, int tailleCombinaison, int maxEssais, boolean modeDebug, int nombreCouleurs) {
        this.nom = nom;
        this.description = description;
        this.tailleCombinaison = tailleCombinaison;
        this.maxEssais = maxEssais;
        this.modeDebug = modeDebug;
        this.nombreCouleurs = nombreCouleurs;
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
