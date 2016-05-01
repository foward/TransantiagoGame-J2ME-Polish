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

public class AboutScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);

  public AboutScreen (MidletTrans midlet) throws Exception {
    super("Acerca del Juego");
    this.midlet = midlet;
    StringItem stringItem = new StringItem(null,"Transchantiago\nVersion 1.0.0\n");
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
