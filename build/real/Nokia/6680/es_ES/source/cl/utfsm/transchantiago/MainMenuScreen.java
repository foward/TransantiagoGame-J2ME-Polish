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

public class MainMenuScreen  extends List implements CommandListener {
  private MidletTrans midlet;
  private Command selectCommand = new Command("Seleccionar", Command.ITEM,1);
  private Command exitCommand = new Command("Salir", Command.EXIT,1);
  
  public MainMenuScreen(MidletTrans midlet) throws Exception {
	//#style mainScreen
    super("Transchantiago",Choice.IMPLICIT, de.enough.polish.ui.StyleSheet.mainscreenStyle );
    this.midlet = midlet;
    //#style mainCommand
    append("Nuevo Juego",null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    //#style mainCommand
    append("Preferencias",null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    //#style mainCommand
    append("Ranking", null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    //#style mainCommand
    append("Ayuda",null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    //#style mainCommand
    append("Creditos",null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    //#style mainCommand
    append("Salir",null, de.enough.polish.ui.StyleSheet.maincommandStyle );
    addCommand(exitCommand);
    addCommand(selectCommand);
    setCommandListener(this);
  }

  public void commandAction(Command c, Displayable d) {
    if (c == exitCommand) {
      midlet.mainMenuScreenQuit();
    } else if (c == selectCommand) {
      processMenu();
    } else {
      processMenu();
    }
  }

  private void processMenu() {
    try {
       List down = (List)midlet.display.getCurrent();
       switch (down.getSelectedIndex()) {
         case 0: scnNewGame(); break;
         case 1: scnSettings(); break;
         case 2: scnHighScore(); break;
         case 3: scnHelp(); break;
         case 4: scnAbout(); break;
         case 5: exit(); break;
       };
    } catch (Exception ex) {      
      midlet.showErrorMsg("null");      
    }
  }
  
  private void exit(){
    midlet.guardarDatos(midlet.datos);
    midlet.notifyDestroyed();
  }

  private void scnNewGame() {
    midlet.gameScreenShow();
  }

  private void scnSettings() {
    midlet.settingsScreenShow();
  }

  private void scnHighScore() {
    midlet.highScoreScreenShow();
  }

  private void scnHelp() {
    midlet.helpScreenShow();
  }

  private void scnAbout() {
    midlet.aboutScreenShow();
  }
}
