package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;

public class RecherchePlusMoins extends Jeu {
    private final int maximum;
    private final int minimum;
    private int[] borneInf;
    private int[] borneSup;

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

    public boolean verifierCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
        //this.nombreEssaisRestants --;
        this.setNombreEssaisRestants(this.getNombreEssaisRestants()-1);
        if (saisieUtilisateur.equals(combinaisonAtrouver)) {
            //this.jeuFini = true;
            this.setJeuFini(true);
            return true;
        }
        else {
            return false;
        }
    }

    public String calculerReponseCombinaison(String saisieUtilisateur, String combinaisonAtrouver) {
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
        //this.nombreEssaisRestants --;
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
                        System.out.println("max:"+maximum+" min:"+minimum+" borneSup:"+borneSup[i]+" borneInf"+borneInf[i]);
                        break;
                    case '-' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) - Math.max(((Character.getNumericValue(combinaisonEnCours.charAt(i)) - borneInf[i]) / 2), 1);
                        borneSup[i] = complement;
                        proposition.append(complement);
                        System.out.println("max:"+maximum+" min:"+minimum+" borneSup:"+borneSup[i]+" borneInf"+borneInf[i]);
                        break;
                    case '+' :
                        complement = Character.getNumericValue(combinaisonEnCours.charAt(i)) + Math.max(((borneSup[i] - (Character.getNumericValue(combinaisonEnCours.charAt(i)))) / 2), 1);
                        proposition.append(complement);
                        borneInf[i] = complement;
                        System.out.println("max:"+maximum+" min:"+minimum+" borneSup:"+borneSup[i]+" borneInf"+borneInf[i]);
                        break;
                }
            }
        }
        return proposition.toString();
    }

    public boolean determinerSiFinJeu(String reponseEvalutionUtilisateur) {
        //jeuFini = true;
        this.setJeuFini(true);
        for (int i = 0; i < reponseEvalutionUtilisateur.length(); i++) {
            if (!Character.valueOf(reponseEvalutionUtilisateur.charAt(i)).equals('=')) {
                //jeuFini = false;
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
        for (int i=0; i < saisieUtilisateur.length(); i++) {
            if (((borneSup[i] == minimum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('-'))) || ((borneInf[i] == maximum) && (Character.valueOf(saisieUtilisateur.charAt(i)).equals('+')))) {
                return false;
            }
        }
        return true;
    }
}
