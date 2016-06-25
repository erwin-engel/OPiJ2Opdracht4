package theatergui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import theater.Theater;
import theater.Voorstelling;
import theaterdata.Connectiebeheer;
import theaterdata.TheaterException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * De klasse TheaterFrame is de grafische interface van het theater. Dit frame
 * bevat een vaste kop, waarin
 * <ul>
 * <li>een keuze voor de voorstelling</li>
 * <li>een label met de naam van de gekozen voorstelling</li>
 * <li>invoervelden voor naam en telefoonnummer van een klant, met bijbehorende
 * labels</li>
 * <li>een knop Plaats om de ingevoerde klant te plaatsen op alle geselecteerde
 * plaatsen</li>
 * </ul>
 * 
 * De interface is opgebouwd uit een aantal panels, gerepresenteerd door
 * verschillende klassen. <br>
 * De klasse VoorstellingsPanel representeert de gui voor de geselecteerde
 * voorstelling. Een voorstellingsPanel bevat een label dat klantgegevens van
 * een aangewezen plaats toont en een zaalPanel. <br>
 * De klasse ZaalPanel toont de zaalbezetting van de geselecteerde voorstelling.
 * Een zaalPanel bevat rij- en stoelnummers, plus voor elke plaats in de zaal
 * een plaatsPanel. <br>
 * De klasse PlaatsPanel toont één plaats voor de geselecteerde voorstelling
 * 
 * @author Open Universiteit
 */
public class TheaterFrame extends JFrame {

  private final SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
  private Theater theater = null;

  private static final long serialVersionUID = 1L;

  /**
   * Het voorstellingsPanel voor de huidige voorstelling
   */
  private VoorstellingsPanel voorstellingsPanel;

  private JPanel jContentPane = null;
  private JComboBox voorstellingsKeuze = null;
  private JLabel naamLabel = null;
  private JTextField naamVeld = null;
  private JLabel telefoonLabel = null;
  private JTextField telefoonVeld = null;
  private JButton plaatsKnop = null;
  private JLabel voorstellingsLabel = null;
  private JLabel foutLabel = null;

  /**
   * This is the default constructor
   * @throws TheaterException 
   */
  public TheaterFrame() throws TheaterException {
    super();
    initialize();
    mijnInit();

    addWindowListener(new WindowAdapter() {
    	@Override
    	public void windowClosing(WindowEvent e) {
    		try {
					Connectiebeheer.closeDB();
				}
				catch (TheaterException ex) {
					System.out.println(ex.getMessage());
				}
    	}
    });
  }

  /**
   * Vult de voorstellingsKeuze en selecteert de eerste voorstelling.
   * @throws TheaterException 
   */
  private void mijnInit() throws TheaterException {

		theater = new Theater("Theater de Schouwburg");

  	if (theater.initialiseerTheater()){
	    setTitle(theater.getNaam());
	    ArrayList<GregorianCalendar> data = theater.geefVoorstellingsData();
	    for (GregorianCalendar datum : data) {
	      voorstellingsKeuze.addItem(fmt.format(datum.getTime()));
	    }
	    voorstellingsKeuze.setSelectedIndex(0);
  	} else {
  		JOptionPane.showMessageDialog(this, "fout in starten theater applicatie",  "", JOptionPane.ERROR_MESSAGE);
  	}
  }
  /**
   * Event handler voor het selecteren van een voorstelling. Er hoeft alleen
   * iets te gebeuren met de event die de voorstelling selecteert, niet met de
   * event die de voorstelling deselecteert.
   * @throws TheaterException 
   */
  private void voorstellingsKeuzeItemStateChanged(ItemEvent e) throws TheaterException {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      String sdatum = (String) voorstellingsKeuze.getSelectedItem();
      GregorianCalendar datum = new GregorianCalendar();
      try {
        datum.setTime(fmt.parse(sdatum));
      } catch (ParseException exc) {
      	foutLabel.setText(exc.getMessage());
      }
      theater.wisselVoorstelling(datum);
      Voorstelling voorstelling = theater.getHuidigeVoorstelling();
      voorstellingsLabel.setText(voorstelling.getNaam());
      if (voorstellingsPanel != null) {
        getContentPane().remove(voorstellingsPanel);
      }
      voorstellingsPanel = new VoorstellingsPanel(theater);
      voorstellingsPanel.setLocation(80, 100);
      getContentPane().add(voorstellingsPanel);
    }
  }
  /**
   * Event handler voor de knop Plaats. Naam en telefoonnummer worden ingelezen,
   * en aan het theater wordt gevraagd om de gereserveerde plaatsen aan een
   * klant met die gegevens toe te wijzen. De invoervelden voor naam en
   * telefoonnummer worden leeg gemaakt.
   * @throws TheaterException 
   */
  private void plaatsKnopAction() throws TheaterException {
    String naam = naamVeld.getText();
    String telefoon = telefoonVeld.getText();
    theater.plaatsKlant(naam, telefoon);

    // maak de velden leeg
    naamVeld.setText("");
    telefoonVeld.setText("");
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setContentPane(getJContentPane());
    this.setTitle("JFrame");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setBounds(new Rectangle(0, 0, 540, 560));
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      foutLabel = new JLabel();
      foutLabel.setBounds(new Rectangle(14, 494, 509, 27));
      foutLabel.setForeground(Color.red);
      foutLabel.setText("");
      voorstellingsLabel = new JLabel();
      voorstellingsLabel.setBounds(new Rectangle(265, 5, 253, 28));
      voorstellingsLabel
          .setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
      voorstellingsLabel.setText("");
      telefoonLabel = new JLabel();
      telefoonLabel.setBounds(new Rectangle(217, 42, 62, 29));
      telefoonLabel.setText("Telefoon:");
      naamLabel = new JLabel();
      naamLabel.setBounds(new Rectangle(13, 42, 50, 29));
      naamLabel.setText("Naam:");
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getVoorstellingsKeuze(), null);
      jContentPane.add(naamLabel, null);
      jContentPane.add(getNaamVeld(), null);
      jContentPane.add(telefoonLabel, null);
      jContentPane.add(getTelefoonVeld(), null);
      jContentPane.add(getPlaatsKnop(), null);
      jContentPane.add(voorstellingsLabel, null);
      jContentPane.add(foutLabel, null);
    }
    return jContentPane;
  }

  /**
   * This method initializes voorstellingsKeuze
   * 
   * @return javax.swing.JComboBox
   */
  private JComboBox getVoorstellingsKeuze() {
    if (voorstellingsKeuze == null) {
      voorstellingsKeuze = new JComboBox();
      voorstellingsKeuze.setBounds(new Rectangle(14, 9, 155, 22));
      voorstellingsKeuze.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent e) {
          try {
						voorstellingsKeuzeItemStateChanged(e);
					}
					catch (TheaterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        }
      });
    }
    return voorstellingsKeuze;
  }

  /**
   * This method initializes naamVeld
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getNaamVeld() {
    if (naamVeld == null) {
      naamVeld = new JTextField();
      naamVeld.setBounds(new Rectangle(66, 42, 128, 29));
    }
    return naamVeld;
  }

  /**
   * This method initializes telefoonVeld
   * 
   * @return javax.swing.JTextField
   */
  private JTextField getTelefoonVeld() {
    if (telefoonVeld == null) {
      telefoonVeld = new JTextField();
      telefoonVeld.setBounds(new Rectangle(281, 42, 91, 29));
    }
    return telefoonVeld;
  }

  /**
   * This method initializes plaatsKnop
   * 
   * @return javax.swing.JButton
   */
  private JButton getPlaatsKnop() {
    if (plaatsKnop == null) {
      plaatsKnop = new JButton();
      plaatsKnop.setBounds(new Rectangle(396, 42, 123, 29));
      plaatsKnop.setText("Plaats");
      plaatsKnop.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          try {
						plaatsKnopAction();
					}
					catch (TheaterException e1) {
						foutLabel.setText(e1.getMessage());
						e1.printStackTrace();
					}
        }
      });
    }
    return plaatsKnop;
  }
  
  public static void main(String[] args) throws TheaterException {
    TheaterFrame gui = new TheaterFrame();
    gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
    gui.setVisible(true);
  }

} // @jve:decl-index=0:visual-constraint="11,17"
