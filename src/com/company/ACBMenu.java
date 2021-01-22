package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ACBMenu {
    private int option;

    public ACBMenu() {
    }

    public int menuPral() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println(" \nMENU PRINCIPAL \n");
            System.out.println("1. Mostra Equips ");
            System.out.println("2. Mostra jugadors d'un determinat equip. ");
            System.out.println("3. Crea team.");
            System.out.println("4. Crear Jugador.");
            System.out.println("5. Crea Partit**********");
            System.out.println("6. Mostra jugadors sense equip");
            System.out.println("7. Assigna jugador a un equip.");
            System.out.println("8. Desvincula jugador  d'un equip.");
            System.out.println("9. Carrega estadístiques.");
            System.out.println("10. Sortir. ");
            System.out.println("Esculli opció: ");

            try {
                this.option = Integer.parseInt(br.readLine());
            } catch (IOException | NumberFormatException var3) {
                System.out.println("valor no vàlid");
                var3.printStackTrace();
            }
        } while(this.option != 1 && this.option != 2 && this.option != 3 && this.option != 4 && this.option != 5 && this.option != 6 && this.option != 7 && this.option != 8 && this.option != 9 && this.option != 10);

        return this.option;
    }

    public Identity autenticacio(int intents) throws IOException {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("==============HEMEROTECA====================================");
        System.out.println("============================================================");
        System.out.println("Avís: tens " + (3 - intents) + " intents per loginarte");
        System.out.println("============================================================");
        System.out.println("Inserta nom del usuari: ");
//        String usuari = br1.readLine();
        String usuari = "postgres";
        System.out.println("Inserta contrasenya: ");
//        String pass = br1.readLine();
        String pass = "yasinhola";
        Identity identity = new Identity(usuari, pass);
        return identity;
    }
}
