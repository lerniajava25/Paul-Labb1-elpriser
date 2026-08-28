# Elpris analysator

## Beskrivning
Det här är ett konsolprogram i Java som hämtar dagens elpriser från elprisetjustnu.se
för valt elområde (SE1, SE2, SE3 eller SE4) och returnerar olika värden baserat på det 
menyval man gör, så som högst, lägsta & medelpris för dagen, sorterar billigaste till
dyraste pris per timme och sorterar ut billigaste 4 timmar i följd för laddningstid

## Funktioner
- välj elområde
- visa lägsta, högsta och genomsnittliga pris för dygnet
- sortera dygnets timmar från billigast till dyrast
- visa bästa sammanhängande 4 timmarsperiod för laddning

## Tekniker
Projektet använder:
- Java 26
- Maven
- HttpClient för API anrop
- Jackson för att läsa JSON data
- Git och github med feature branches och pull requests

## Reflektioner
Jag upplever att Java är ett väldigt strikt programmeringsspråk till skillnad från
JavaScript som är så mycket mer förlåtande, framförallt när det gäller datatyper,
returvärden och hur metoder och objekt är uppbyggda.

Att jobba med Git och branches är sedan tidigare bekant för mig från tidigare
JavaScript projekt, så den delen kändes ganska naturlig.

Det som var mer nytt i den här uppgiften var att arbeta med HttpClient för 
API anrop och jackson för att läsa JSON data i Java.