package theater;

import java.util.*;

import theaterdata.*;

/**
 * Representeert (de kassa van) een theater.
 * @author Open Universiteit
 */
public class Theater {
  public static final int AANTALPERRIJ = 10;
  public static final int AANTALRIJEN = 15;
  private String naam = null;
  private Voorstelling huidigeVoorstelling = null;
  
 
  /**
   * 
   * @param naam
   * @throws TheaterException
   */
  public Theater(String naam) throws TheaterException {
    this.naam = naam;
	}

  /**
   * Geeft de naam van het theater.
   * @return naam van het theater
   */
  public String getNaam() {
    return naam;
  }
  
  /**
   * Geeft een lijst van data waarop voorstellingen zijn gepland.
   * @return lijst met data.
   * @throws TheaterException 
   */
  public ArrayList<GregorianCalendar> geefVoorstellingsData() throws TheaterException {
    return Voorstellingbeheer.geefVoorstellingsData();
  }
  
  /**
   * Wisselt de huidige voorstelling naar voorstelling met gegeven datum.
   * @param datum datum van gevraagde voorstelling
   * @throws TheaterException 
   */
  public void wisselVoorstelling(GregorianCalendar datum) throws TheaterException {
    huidigeVoorstelling = Voorstellingbeheer.geefVoorstelling(datum);
  }

  /**
   * Geeft de huidige voorstelling.
   * @return de huidige voorstelling
   */
  public Voorstelling getHuidigeVoorstelling() {
    return huidigeVoorstelling;
  }
  
  /**
   * Plaatst een klant op alle gereserveerde stoelen van de huidige uitvoering.
   * Wanneer een klant nog niet bestaat, dan wordt deze eerst gecreeerd.
   * @param naam naam van klant
   * @param telefoon telefoonnummer van klant
   * @throws TheaterException 
   */
  public void plaatsKlant(String naam, String telefoon) throws TheaterException {
  	String naamopgemaakt = naam.trim();
  	if (naamopgemaakt.length() < 1 || telefoon.length() < 1) {
  		throw new TheaterException("vul juiste naam en telefoonnummer in");
  	}
    Klant klant = Klantbeheer.geefKlant(naamopgemaakt, telefoon);
    huidigeVoorstelling.plaatsKlant(klant);
  }

  /**
   * Verandert de reserveringsstatus (VRIJ - GERESERVEERD) van een plaats
   * in de huidige voorstelling.
   * @param rijnummer rijnummer van plaats
   * @param stoelnummer stoelnummer van plaats
   * @return true als verandering is gelukt, anders false
   */
  public boolean veranderReservering(int rijnummer, int stoelnummer) {
    return huidigeVoorstelling.veranderReservering(rijnummer, stoelnummer);
    
  }

  /**
   * Geeft informatie over een plaats in de huidige voorstelling.
   * @param rijnummer rijnummer van plaats
   * @param stoelnummer stoelnummer van plaats
   * @return informatie over plaats
   */
  public String geefPlaatsInfo(int rijnummer, int stoelnummer) {
    Plaats plaats = huidigeVoorstelling.getPlaats(rijnummer, stoelnummer);
    return plaats.toString();
  }
  public void initialiseerTheater() throws TheaterException{
  	Connectiebeheer.openDB();
    Klantbeheer.init();
    Voorstellingbeheer.init();
  }
}
