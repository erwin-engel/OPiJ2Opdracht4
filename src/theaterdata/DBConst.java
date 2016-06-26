package theaterdata;

/**
 * Deze klasse is verantwoordelijk voor het beheer van alle constanten die nodig zijn voor
 * het beheer van theaterdata
 * @author erwin
 *
 */
/*public class DBConst {

}*/
public class DBConst {
	// lokaal verbinden
	public static final String PADNAAM = "theater";
	public static final String DRIVERNAAM = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost/" + PADNAAM;
	public static final String GEBRUIKERSNAAM = "cppjava";
	public static final String WACHTWOORD = "theater";
	// met NAS verbinden
/*	public static final String PADNAAM = "theater";
	public static final String DRIVERNAAM = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mariadb://192.168.178.20/" + PADNAAM;
	public static final String GEBRUIKERSNAAM = "root";
	public static final String WACHTWOORD = "";
	*/
	
	// database gerelateerde foutmeldingen
	public static final String DBOPENERROR = 
			"Er heeft zich een fout voorgedaan bij het openen van de database: ";
	public static final String DBCLOSEERROR = 
			"Er heeft zich een fout voorgedaan bij het sluiten van de database: ";
	public static final String DBCONNECTERROR = 
			"Er heeft zich een fout voorgedaan met de connectie naar de database: ";
	public static final String DBPREPSTATERROR = 
			"Er heeft zich een fout voorgedaan tijdens de voorbereiding van de benodigde database acties.";
	public static final String DBGENERICERROR = 
			"Er heeft zich een fout voorgedaan in de verwerking met database";
	public static final String DBSELECTERROR = 
			"Er heeft zich een fout voorgedaan tijdens een selectie uit de database";
	public static final String DBCREATEERROR = 
			"Er heeft zich een fout voorgedaan tijdens een update naar de database: ";

	// SQL statements
	public static final String SELVOORSTELLINGEN =
			"SELECT datum, naam FROM voorstelling";
	public static final String SELVOORSTELLING =
			"SELECT datum, naam FROM voorstelling WHERE datum = ?";
	public static final String SELBEZETTINGEN =
			"SELECT resnummer, voorstelling, rijnummer, stoelnummer, klant FROM bezetting WHERE voorstelling = ?";
	public static final String INSBEZETTING =
			("INSERT INTO `theater`.`bezetting` (`voorstelling`, `rijnummer`, `stoelnummer`, `klant`) VALUES (?, ?, ?, ?)"); 
	public static final String SELKLANTNR =
			"SELECT klantnummer, naam, telefoon FROM klant WHERE klantnummer = ?";
	public static final String SELKLANTNMTEL =
			"SELECT klantnummer, naam, telefoon FROM klant WHERE naam = ? AND telefoon = ?";
	public static final String SELKLANTMAX =
			"SELECT max(Klantnummer) from klant";
	public static final String INSKLANT =
			"INSERT INTO `theater`.`klant` (`klantnummer`, `naam`, `telefoon`) VALUES (?, ?, ?) ;";
}
