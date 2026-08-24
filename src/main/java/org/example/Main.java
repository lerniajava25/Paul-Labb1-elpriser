package org.example;

public class Main {

    static void showMenu() {
        IO.println("\n1. Välj elområde (SE1, SE2, SE3, SE4)");
        IO.println("2. Min, Max och Medelpris");
        IO.println("3. Sortera priser (lägst till högst)");
        IO.println("4. Bästa laddningstid (4h sammanhängande)");
        IO.println("e. Avslut\n");
    }

    static String chooseArea() {
        String elChoice = "";

        boolean elChoosingArea = true;
        while (elChoosingArea) {
            elChoice = IO.readln("Välj elområde (SE1, SE2, SE3, SE4): ");
            if (elChoice.equalsIgnoreCase("SE1") ||
                    elChoice.equalsIgnoreCase("SE2") ||
                    elChoice.equalsIgnoreCase("SE3") ||
                    elChoice.equalsIgnoreCase("SE4")) {
                elChoice = elChoice.toUpperCase();
                IO.println("Du valde " + elChoice);
                elChoosingArea = false;
            } else {
                IO.println("Du har valt ett ogiltigt område. Försök igen!\n");
            }
        }
        return elChoice;
    }

    static void main() {
        String elChoice = "";
        boolean menuRunning = true;

        while (menuRunning) {

            showMenu();

            String choice = IO.readln("Välj ett alternativ från menyn: ").toLowerCase();

            switch (choice) {
                case "1":
                    elChoice = chooseArea();
                    break;
                case "2":
                    IO.println("Min, Max och Medelpris");
                    break;
                case "3":
                    IO.println("Sortera priser (lägst till högst)");
                    break;
                case "4":
                    IO.println("Bästa laddningstid (4h sammanhängande)");
                    break;
                case "e":
                    IO.println("Avslut");
                    menuRunning = false;
                    break;
                default:
                    IO.println("Ogiltigt menyval. Välj ett utav menys alternativ!");
                    break;
            }

        }

    }
}
