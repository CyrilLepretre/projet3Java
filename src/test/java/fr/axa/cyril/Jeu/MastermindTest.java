package fr.axa.cyril.Jeu;

import fr.axa.cyril.Menu.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MastermindTest {
    String liste;
    Configuration conf;

    @BeforeEach
    public void init(){
        liste = "RVBJMNGCOP";
        conf = new Configuration();
    }

    @Test
    public void doNothing() {
    }

    /*@Test
    public void GivenRWhenGenererCombinaisonThenRetourne1() {
        assertEquals(new Mastermind().genererCombinaison(1), "R");
    }*/

    /*@Test
    public void Given1EnBorneSupWhenChiffreRandomThenRetourne1() {
        assertEquals(new Mastermind(conf).genererChiffreRandom(1),1);
    }

    @Test
    public void Given0EnBorneSupWhenChiffreRandomThenAfficheErreur() {
        assertEquals(new Mastermind(conf).genererChiffreRandom(0),0);
    }

    @Test
    public void GivenCombinaisonDe1WhenGenererCombinaisonThenRenvoieUnCharDeLaliste() {
        char c = new Mastermind(conf).genererCombinaison(1).charAt(0);
        assertNotEquals(liste.lastIndexOf(c), -1);
    }

    @Test
    public void GivenCombinaisonDe5WhenGenererCombinaisonThenRenvoieUneChaineDe5Char() {
        String listeGeneree = new Mastermind(conf).genererCombinaison(5);
        assertEquals(listeGeneree.length(),5);
    }*/
}