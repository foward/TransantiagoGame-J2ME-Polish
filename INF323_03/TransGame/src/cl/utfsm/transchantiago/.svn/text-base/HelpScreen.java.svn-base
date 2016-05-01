/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago;

/**
 *
 * @author Francisco
 */
import javax.microedition.lcdui.*;

public class HelpScreen extends Form implements CommandListener {
  private MidletTrans midlet;

  private Command backCommand = new Command("Volver", Command.BACK, 1);

  
  //TODO agregar ayuda
  public HelpScreen (MidletTrans midlet) throws Exception {
    super("Ayuda");
    this.midlet = midlet;
    StringItem stringItem = new StringItem(null,
    "Ayuda ...con la tecla..."
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