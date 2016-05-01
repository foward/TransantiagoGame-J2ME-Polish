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

public class HighScoreScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);
  StringItem stringItem;

  public HighScoreScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Ranking");
    this.midlet = midlet;
  //#style textStyle
    stringItem = new StringItem(null,"");
    append(stringItem);
    addCommand(backCommand);
    setCommandListener(this);
  }

  public void commandAction(Command c, Displayable d) {
    if (c == backCommand) {
      midlet.mainMenuScreenShow(null);
      return;
    }
  }

  public void init() throws Exception {
    stringItem.setText(buildHighScore());
  }


  private String buildHighScore() {
    String result = "1: " + this.midlet.datos.nombre1 + "\n" +
		"          " + String.valueOf(this.midlet.datos.puntaje1) + "\n" +
                "2: " + this.midlet.datos.nombre2 + "\n" +
		"          " + String.valueOf(this.midlet.datos.puntaje2) + "\n" +
                "3: " + this.midlet.datos.nombre3 + "\n" +
		"          " + String.valueOf(this.midlet.datos.puntaje3) + "\n" +
                "4: " + this.midlet.datos.nombre4 + "\n" +
		"          " + String.valueOf(this.midlet.datos.puntaje4) + "\n" +
                "5: " + this.midlet.datos.nombre5 + "\n" +
		"          " + String.valueOf(this.midlet.datos.puntaje5) + "\n";
    return result;
  }
}