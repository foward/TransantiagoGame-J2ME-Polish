/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago;

/**
 *
 * @author eraddatz
 */
import javax.microedition.lcdui.*;

public class SettingsScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);

  private ChoiceGroup difficulty;
  private ChoiceGroup vibration;
  
  private Gauge volume;


  public SettingsScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Preferencias");
    this.midlet = midlet;
    //#style textStyle
    StringItem diffText = new StringItem(null,"Dificultad:");
    //#style choiceStyle
    difficulty = new ChoiceGroup (null, List.EXCLUSIVE, new String[] {"Baja","Media","Alta"}, null);
    difficulty.setSelectedIndex(this.midlet.datos.dificultad, true);
    //#style textStyle
    StringItem vibText = new StringItem(null,"Vibracion:");
    //#style choiceStyle
    vibration = new ChoiceGroup (null, List.EXCLUSIVE, new String[] {"No","Si"}, null);
    vibration.setSelectedIndex(this.midlet.datos.vibracion, true);
    //#style textStyle
    StringItem volText = new StringItem(null,"Volumen:");
    //#style gaugeStyle
    volume = new Gauge(null, true, 10, 0);
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