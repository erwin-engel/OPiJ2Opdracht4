import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import theaterdata.Connectiebeheer;
import theaterdata.TheaterException;

public class TheaterDatabeheer {
	
	private static Date naarSQLDatum (GregorianCalendar gcDatum ){
		return (new Date(gcDatum.getTimeInMillis()));
	}

	private static GregorianCalendar naarGCDatum (Date sqlDatum ){
		GregorianCalendar gcDatum = new GregorianCalendar();
		gcDatum.setTimeInMillis(sqlDatum.getTime());
		return (gcDatum);
	}

	private static String geefJmdDatum(GregorianCalendar datum){
		SimpleDateFormat datumFormaat = new SimpleDateFormat("dd-MM-yyyy");
		String jmdDatum = datumFormaat.format(datum.getTime());
		return (jmdDatum.toString());
	}
	
	private static void throwExceptie(Exception e, String foutmelding) throws TheaterException{
		Connectiebeheer.closeDB();
		e.printStackTrace();
		throw new TheaterException(foutmelding);
	}

}
