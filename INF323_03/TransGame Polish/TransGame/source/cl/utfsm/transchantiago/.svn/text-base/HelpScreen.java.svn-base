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

public class HelpScreen extends Form implements CommandListener {
  private MidletTrans midlet;

  private Command backCommand = new Command("Volver", Command.BACK, 1);

  
  //TODO agregar ayuda
  public HelpScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Ayuda");
    this.midlet = midlet;
  //#style textStyle
    StringItem stringItem = new StringItem(null,
    "Con la tecla...\n\n" +
            "2: Acelerar\n" +
            "8: Frenar\n" +
            "4: Virar Izquierda\n" +
            "6: Virar Derecha\n" +
            "5: Nitro"
                                                );
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
}