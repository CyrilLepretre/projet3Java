package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecherchePlusMoinsTest {
    private Configuration conf;
    private RecherchePlusMoins recherchePlusMoins;

    @BeforeEach
    public void init(){
        conf = new Configuration();
        conf.initConfiguration("/config.properties" , "recherche");
        recherchePlusMoins = new RecherchePlusMoins(conf);
    }

    @Test
    public void Given_Vide_When_ProposerCombinaison_Then_RetourneDes5() {
        assertEquals("55555", recherchePlusMoins.proposerCombinaison("",null));
    }

    @Test
    public void Given_QueDes5EtToutEstPlus_When_ProposerCombinaison_Then_RetourneDes7() {
        assertEquals("77777", recherchePlusMoins.proposerCombinaison("55555","+++++"));
    }

    @Test
    public void Given_QueDes5EtToutEstMoins_When_ProposerCombinaison_Then_RetourneDes3() {
        assertEquals("33333", recherchePlusMoins.proposerCombinaison("55555","-----"));
    }

    @Test
    public void Given_QueDes2EtToutEstMoins_When_ProposerCombinaison_Then_RetourneDes1() {
        assertEquals("11111", recherchePlusMoins.proposerCombinaison("22222","-----"));
    }

    @Test
    public void Given_QueDes8EtToutEstPlus_When_ProposerCombinaison_Then_RetourneDes9() {
        assertEquals("99999", recherchePlusMoins.proposerCombinaison("88888","+++++"));
    }

    @Test
    public void Given_Des8etDes2EtPlusMoins_When_ProposerCombinaison_Then_RetourneDes9et1() {
        assertEquals("19191", recherchePlusMoins.proposerCombinaison("28282","-+-+-"));
    }

    @Test
    public void Given_BonneReponse_When_VerifierCombinaison_Then_RetourneTrue() {
        assertEquals(true, recherchePlusMoins.verifierCombinaison("45453", "45453"));
    }

    @Test
    public void Given_MauvaiseReponse_When_VerifierCombinaison_Then_RetourneFalse() {
        assertEquals(false, recherchePlusMoins.verifierCombinaison("88453", "45453"));
    }

    @Test
    public void Given_22588_When_CalculerReponseCombinaisonQueDes5_Then_RetournePlusPlusEgalMoinsMoins() {
        assertEquals("++=--", recherchePlusMoins.calculerReponseCombinaison("22588", "55555"));
    }

    @Test
    public void Given_22588_When_CalculerReponseCombinaison22588_Then_RetourneQueDesEgal() {
        assertEquals("=====", recherchePlusMoins.calculerReponseCombinaison("22588", "22588"));
    }

}