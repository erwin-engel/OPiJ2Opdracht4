package theaterdata;

/**
 * Exceptionklasse voor exceptie afhandeling in theater
 * @author Erwin Engel
 *
 */
public class TheaterException extends Exception {
  
  public TheaterException(){
    super();
  }
  
  public TheaterException(String s){
    super(s);
  }
}
