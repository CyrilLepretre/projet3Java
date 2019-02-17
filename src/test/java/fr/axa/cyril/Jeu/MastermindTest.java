package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MastermindTest {
    private String liste;
    private Configuration conf;
    private Mastermind mastermind;

    @BeforeEach
    private void init(){
        conf = new Configuration();
        conf.initConfiguration("/config.properties" , "mastermind");
        mastermind = new Mastermind(conf);
    }

    @Test
    public void doNothing() {
    }

    @Test
    public void Given_1EnBorneSup_When_ChiffreRandom_Then_Retourne1() {
        assertEquals(1, mastermind.genererChiffreRandom(1));
    }

    @Test
    public void Given_0EnBorneSup_When_ChiffreRandom_Then_AfficheErreur() {
        assertEquals(0, mastermind.genererChiffreRandom(0));
    }

    @Test
    public void Given_CombinaisonDe5_When_GenererCombinaison_Then_RenvoieUneChaineDe5Char() {
        String listeGeneree = mastermind.genererCombinaison(5);
        assertEquals(5, listeGeneree.length());
    }

    @Test
    public void Given_NimporteQuoi_When_VerifierErreurFonctionnelle_Then_RetourneToujoursTrue() {
        assertEquals(true, mastermind.verifierErreurFonctionnelle("test"));
    }

    @Test
    public void Given_ToutBienPlace_When_DeterminerSiFinJeu_Then_RetourneTrue() {
        assertEquals(true, mastermind.determinerSiFinJeu("4-0"));
    }

    @Test
    public void Given_PasTousBienPlaces_When_DeterminerSiFinJeu_Then_RetourneFalse() {
        assertEquals(false, mastermind.determinerSiFinJeu("3-1"));
    }

    @Test
    public void Given_BonneReponse_When_CalculerReponseCombinaison_Then_RetourneStringToutOK() {
        assertEquals("4 bien placé(s), 0 présent(s)", mastermind.calculerReponseCombinaison("RRVB", "RRVB"));
    }

    @Test
    public void Given_2bienPlaces2MalPlaces_When_CalculerReponseCombinaison_Then_RetourneString2bienPlaces2malplaces() {
        assertEquals("2 bien placé(s), 2 présent(s)", mastermind.calculerReponseCombinaison("RRVB", "RRBV"));
    }

    @Test
    public void Given_BonneReponse_When_VerfierCombinaison_Then_RetourneTrue() {
        assertEquals(true, mastermind.verifierCombinaison("RRVB", "RRVB"));
    }

    @Test
    public void Given_MauvaiseReponse_When_VerfierCombinaison_Then_RetourneFalse() {
        assertEquals(false, mastermind.verifierCombinaison("BBBB", "RRVB"));
    }
}