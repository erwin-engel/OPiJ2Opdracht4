package theaterdata;

import java.util.GregorianCalendar;
/**
 * Representeert een bezetting zoals aanwezig in de theater database. Een bezetting object wordt alleen
 * gebruikt binnen theater's datatlaag: theaterdata
 * @author Erwin Engel
 */
public class Bezetting {
	private int rijnummer = 0;
	private GregorianCalendar gcDatum = null;
	private int stoelnummer = 0;
	private int klantnummer = 0;
	/**
	 * Maakt nieuw instantie Bezetting	
	 * @param gcDatum, formaat is Gregorian Calendar
	 * @param rijnummer
	 * @param stoelnummer
	 * @param klantnummer
	 */
	public Bezetting (GregorianCalendar gcDatum, int rijnummer, int stoelnummer, int klantnummer){
		this.gcDatum = gcDatum;
		this.rijnummer = rijnummer;
		this.stoelnummer = stoelnummer;
		this.klantnummer = klantnummer;
	}
	/**
	 * geeft rijnummer
	 * @return rijnummer
	 */
	public int getRijnummer() {
		return rijnummer;
	}
	/**
	 * geeft stoelnummer
	 * @return stoelnummer
	 */
	public int getStoelnummer() {
		return stoelnummer;
	}
	/**
	 * geeft klantnummer
	 * @return klantnummer
	 */
	public int getKlantnummer() {
		return klantnummer;
	}
	/**
	 * maakt string representatie
	 */
	public String toString(){
		return ("voorstelling datum: " + TheaterDatabeheer.geefJmdDatum(this.gcDatum) + ", rijnummer: " + rijnummer + ", stoelnummer: "
				+ stoelnummer + ", klantnummer: " + klantnummer);
	}
}
