package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    static String todaysDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatedDate = DateTimeFormatter.ofPattern("yyyy/MM-dd_");

        String dateForAPI = today.format(formatedDate);

        return dateForAPI;
    }

    static String buildApiUrl(String elChoice) {
        String date = todaysDate();
        String url = "https://www.elprisetjustnu.se/api/v1/prices/" + date + elChoice + ".json";

        return url;
    }

    static String fetchPrices(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            return response.body();
        } catch (IOException e) {
            IO.println("Nätverksfel: " + e.getMessage());
            return "";
        } catch (InterruptedException e) {
            IO.println("Anropet avbröts");
            return "";
        }
    }

    static ElectricityPrice[] parsePrices(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, ElectricityPrice[].class);
        } catch (IOException e) {
            IO.println("Kunde inte läsa JSON: " + e.getMessage());
            return new ElectricityPrice[0];
        }
    }

    static boolean hasPrices(ElectricityPrice[] prices) {
        if (prices.length == 0) {
            IO.println("Du måste välja elområde först!");
            return false;
        }

        return true;
    }

    static void showMinMaxAvg(ElectricityPrice[] prices) {
        double min = prices[0].sekPerKwh();
        double max = prices[0].sekPerKwh();
        double sum = 0;


        for (int i = 0; i < prices.length; i++) {
            double currentPrice = prices[i].sekPerKwh();

            if (currentPrice < min) {
                min = currentPrice;
            }

            if (currentPrice > max) {
                max = currentPrice;
            }

            sum += currentPrice;
        }

        double avg = sum / prices.length;

        IO.println("Lägsta pris: " + String.format("%.2f", min * 100) + " öre/kWh");
        IO.println("Högsta pris: " + String.format("%.2f", max * 100) + " öre/kWh");
        IO.println("Medel pris: " + String.format("%.2f", avg * 100) + " öre/kWh");
    }

    static HourlyPrice[] calculateHourlyPrices(ElectricityPrice[] prices) {
        HourlyPrice[] hourlyPrices = new HourlyPrice[24];

        for (int hour = 0; hour < 24; hour++) {
            int startIndex = hour * 4;

            double price1 = prices[startIndex].sekPerKwh();
            double price2 = prices[startIndex + 1].sekPerKwh();
            double price3 = prices[startIndex + 2].sekPerKwh();
            double price4 = prices[startIndex + 3].sekPerKwh();

            double hourlyAvg = (price1 + price2 + price3 + price4) / 4;

            hourlyPrices[hour] = new HourlyPrice(hour, hourlyAvg);
        }

        return hourlyPrices;
    }

    static void showSortedPrices(ElectricityPrice[] prices) {
        HourlyPrice[] hourlyPrices = calculateHourlyPrices(prices);

        Arrays.sort(
                hourlyPrices,
                Comparator.comparingDouble(HourlyPrice::price)
        );

        for (int i = 0; i < hourlyPrices.length; i++) {
            int hour = hourlyPrices[i].hour();

            String formattedStartHour = String.format("%02d", hour);
            String formattedEndHour = String.format("%02d", hour + 1);

            IO.println(
                    formattedStartHour + ":00-" + formattedEndHour + ":00: " +
                            String.format("%.2f", hourlyPrices[i].price() * 100) +
                            " öre/kWh"
            );
        }


    }


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
        ElectricityPrice[] prices = new ElectricityPrice[0];

        while (menuRunning) {

            showMenu();

            String choice = IO.readln("Välj ett alternativ från menyn: ").toLowerCase();

            switch (choice) {
                case "1":
                    elChoice = chooseArea();

                    String url = buildApiUrl(elChoice);
                    String json = fetchPrices(url);
                    prices = parsePrices(json);

                    IO.println("Antal prisposter: " + prices.length);
                    break;
                case "2":
                    if (hasPrices(prices)) {
                        IO.println("Min, Max och Medelpris i område: " + elChoice);
                        showMinMaxAvg(prices);
                    }
                    break;
                case "3":
                    if (hasPrices(prices)) {
                        IO.println("Sorterar priser (lägst till högst) i område: " + elChoice);
                        showSortedPrices(prices);
                    }
                    break;
                case "4":
                    if (hasPrices(prices)) {
                        IO.println("Bästa laddningstid (4h sammanhängande) i område: " + elChoice);
                    }
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
