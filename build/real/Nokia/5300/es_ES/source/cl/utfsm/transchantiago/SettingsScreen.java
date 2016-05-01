/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago;

/**
 *
 * @author eraddatz
 */
import javax.microedition.lcdui.CommandListener;  					import javax.microedition.lcdui.AlertType; 					import javax.microedition.lcdui.Canvas; 					import javax.microedition.lcdui.Command; 					import javax.microedition.lcdui.Display; 					import javax.microedition.lcdui.Displayable; 					import javax.microedition.lcdui.Font; 					import javax.microedition.lcdui.Graphics; 					import javax.microedition.lcdui.Image;  					import de.enough.polish.ui.*;

public class SettingsScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);

  private ChoiceGroup difficulty;
  private ChoiceGroup vibration;
  
  private Gauge volume;


  public SettingsScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Preferencias", de.enough.polish.ui.StyleSheet.secondscreenStyle );
    this.midlet = midlet;
    //#style textStyle
    StringItem diffText = new StringItem(null,"Dificultad:", de.enough.polish.ui.StyleSheet.textstyleStyle );
    //#style choiceStyle
    difficulty = new ChoiceGroup (null, List.EXCLUSIVE, new String[] {"Baja","Media","Alta"}, null, de.enough.polish.ui.StyleSheet.choicestyleStyle );
    difficulty.setSelectedIndex(this.midlet.datos.dificultad, true);
    //#style textStyle
    StringItem vibText = new StringItem(null,"Vibracion:", de.enough.polish.ui.StyleSheet.textstyleStyle );
    //#style choiceStyle
    vibration = new ChoiceGroup (null, List.EXCLUSIVE, new String[] {"No","Si"}, null, de.enough.polish.ui.StyleSheet.choicestyleStyle );
    vibration.setSelectedIndex(this.midlet.datos.vibracion, true);
    //#style textStyle
    StringItem volText = new StringItem(null,"Volumen:", de.enough.polish.ui.StyleSheet.textstyleStyle );
    //#style gaugeStyle
    volume = new Gauge(null, true, 10, 0, de.enough.polish.ui.StyleSheet.gaugestyleStyle );
    volume.setValue(this.midlet.datos.volumen);
    append(diffText);
    append(difficulty);
    append(vibText);
    append(vibration);
    append(volText);
    append(volume);
    addCommand(backCommand);
    setCommandListener(this);
  }

  public void commandAction(Command c, Displayable d) {
    if (c == backCommand) {
        midlet.datos.setDificultad(difficulty.getSelectedIndex());
        midlet.datos.setVibracion(vibration.getSelectedIndex());
        midlet.datos.setVolumen(volume.getValue());
        midlet.guardarDatos(midlet.datos);
        midlet.mainMenuScreenShow(null);
    }
  }

  public void init() throws Exception {
  }

  private void processMenu() {

  }
}
