package fr.axa.cyril;

import fr.axa.cyril.Menu.Menu;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.initConfiguration();
        menu.affichageMenu();
    }
}
