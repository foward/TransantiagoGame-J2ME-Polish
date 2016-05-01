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

public class AboutScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);

  public AboutScreen (MidletTrans midlet) throws Exception {
	//#style secondScreen
    super("Creditos", de.enough.polish.ui.StyleSheet.secondscreenStyle );
    this.midlet = midlet;
  //#style textStyle
    StringItem stringItem = new StringItem(null,"Transchantiago\nVersion 0.95\n\nDesarrollado por:\n" +
    		"Erich Raddatz\nFrancisco Riveros\nJose Miguel Tobar\nMarcelo Salazar\n\n" +
		"Seminario de Desarrollo de Software", de.enough.polish.ui.StyleSheet.textstyleStyle );
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
