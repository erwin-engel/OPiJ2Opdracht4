package theaterdata;

import java.util.ArrayList;
import theater.Klant;

/**
 * Deze klasse die klanten beheert.
 * Deze klasse moet gewijzigd worden zodat ipv ArrayList database gebruikt wordt.
 */
public class Klantbeheer {  
  private static int hoogsteKlantnummer = 0;
  private static ArrayList<Klant> klanten = new ArrayList<Klant>();

  /**
   * Initialiseert de klanten. Hier hoeft nu nog niets te gebeuren.
   */
  public static void init() {    
  }
  
  /**
   * Genereert het volgende beschikbare klantnummer.
   * @return nieuw klantnummer
   */
  public static int getVolgendKlantNummer() {
    return hoogsteKlantnummer++;
  }
  
  /**
   * Geeft een klant met de gegeven naam en het gegeven telefoonnummer
   * Als de klant al in de lijst zat, wordt die teruggegeven; anders
   * wordt er een nieuwe klant gemaakt.
   * @param naam  naam van de klant
   * @param telefoon  telefoonnummer van de klant
   * @return  een klant me de ingevoerde naam en telefoon.
   */
  public static Klant geefKlant(String naam, String telefoon) {
    Klant klant = zoekKlant(naam, telefoon);
    if (klant == null) {
      klant = nieuweKlant(naam, telefoon);
    }
    return klant;
  }
  
  /**
   * Zoekt klant met gegeven naam in de lijst met klanten.
   * @param naam naam van te zoeken klant
   * @param telefoon telefoonnummer van te zoeken klant
   * @return de klant of null wanneer klant niet is gevonden
   */
  private static Klant zoekKlant(String naam, String telefoon) {
    for (Klant k : klanten) {
      if (k.getNaam().equals(naam) && k.getTelefoon().equals(telefoon)) {
        return k;
      }
    }
    return null;
  }
  
  /**
   * Voegt een nieuwe klant toe aan theater.
   * @param naam  naam van de nieuwe klant
   * @param telefoon telefoonnummer van de nieuwe klant
   */
  private static Klant nieuweKlant(String naam, String telefoon) {
    int knr = getVolgendKlantNummer();
    Klant k = new Klant(knr, naam, telefoon);
    klanten.add(k);
    return k;
  }
 
}
