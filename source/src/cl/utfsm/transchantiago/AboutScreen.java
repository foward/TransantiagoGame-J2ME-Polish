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

public class AboutScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);

  public AboutScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Creditos");
    this.midlet = midlet;
  //#style textStyle
    StringItem stringItem = new StringItem(null,"Transchantiago\nVersion 0.95\n\nDesarrollado por:\n" +
    		"Erich Raddatz\nFrancisco Riveros\nJose Miguel Tobar\nMarcelo Salazar\n\n" +
		"Seminario de Desarrollo de Software");
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
