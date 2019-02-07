package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecherchePlusMoinsTest {
    private Configuration conf;
    //private String listePossible = "0123456789";

    @BeforeEach
    public void init(){
        conf = new Configuration();
        conf.initConfiguration("/config.properties" , "recherche");
    }

    @Test
    public void Given_Vide_When_ProposerCombinaison_Then_RetourneDes5() {
        assertEquals("55555", new RecherchePlusMoins(conf).proposerCombinaison("",null));
    }

    @Test
    public void Given_QueDes5EtToutEstPlus_When_ProposerCombinaison_Then_RetourneDes7() {
        assertEquals("77777", new RecherchePlusMoins(conf).proposerCombinaison("55555","+++++"));
    }

    @Test
    public void Given_QueDes5EtToutEstMoins_When_ProposerCombinaison_Then_RetourneDes3() {
        assertEquals("33333", new RecherchePlusMoins(conf).proposerCombinaison("55555","-----"));
    }

    @Test
    public void Given_QueDes2EtToutEstMoins_When_ProposerCombinaison_Then_RetourneDes1() {
        assertEquals("11111", new RecherchePlusMoins(conf).proposerCombinaison("22222","-----"));
    }

    @Test
    public void Given_QueDes8EtToutEstPlus_When_ProposerCombinaison_Then_RetourneDes9() {
        assertEquals("99999", new RecherchePlusMoins(conf).proposerCombinaison("88888","+++++"));
    }

    @Test
    public void Given_Des8etDes2EtPlusMoins_When_ProposerCombinaison_Then_RetourneDes9et1() {
        assertEquals("19191", new RecherchePlusMoins(conf).proposerCombinaison("28282","-+-+-"));
    }

}