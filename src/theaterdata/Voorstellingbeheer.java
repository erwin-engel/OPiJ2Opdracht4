package theaterdata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import theater.Voorstelling;

/**
 * Klasse die met voorstellingen beheert. Op elke datum is er maar één
 * voorstelling. Deze klasse moet gewijzigd worden zodat ArrayList vervangen
 * wordt door gebruik database.
 */
public class Voorstellingbeheer {
  // tijdelijk; in databaseversie niet meer nodig
  private static ArrayList<Voorstelling> voorstellingen = new ArrayList<Voorstelling>();

  /**
   * Vult voorstellingbeheer met een aantal voorstellingen.
   */
  public static void init() {
    voorstellingen.add(new Voorstelling("Moeders en zonen", new GregorianCalendar(2016,
        Calendar.SEPTEMBER, 16)));
    voorstellingen.add(new Voorstelling("Een Engelse Mozart", new GregorianCalendar(2016,
        Calendar.OCTOBER, 6)));
  }
  
  /**
   * Levert alle data op waarop voorstellingen zijn (voor zover die data in de
   * toekomst liggen).
   * 
   * @return lijst met data van voorstellingen
   */
  public static ArrayList<GregorianCalendar> geefVoorstellingsData() {
    GregorianCalendar nu = new GregorianCalendar();
    ArrayList<GregorianCalendar> data = new ArrayList<GregorianCalendar>();
    for (Voorstelling voorstelling : voorstellingen) {
      GregorianCalendar datum = voorstelling.getDatum();
      if (datum.after(nu)) {
        data.add(datum);
      }
    }
    return data;
  }

  /**
   * Zoekt een voorstelling op de gegeven datum.
   * 
   * @param datum
   * @return een voorstelling op de gegeven datum of null wanneer die
   *         voorstelling er niet is.
   */
  public static Voorstelling geefVoorstelling(GregorianCalendar datum) {
    for (Voorstelling voorstelling : voorstellingen) {
      if (voorstelling.getDatum().equals(datum)) {
        return voorstelling;
      }
    }
    return null;
  }


}
