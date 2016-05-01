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

public class MainMenuScreen  extends List implements CommandListener {
  private MidletTrans midlet;
  private Command selectCommand = new Command("Seleccionar", Command.ITEM,1);
  private Command exitCommand = new Command("Salir", Command.EXIT,1);
  
  public MainMenuScreen(MidletTrans midlet) throws Exception {
	//#style mainScreen
    super("Transchantiago",Choice.IMPLICIT);
    this.midlet = midlet;
    //#style mainCommand
    append("Nuevo Juego",null);
    //#style mainCommand
    append("Preferencias",null);
    //#style mainCommand
    append("Ranking", null);
    //#style mainCommand
    append("Ayuda",null);
    //#style mainCommand
    append("Creditos",null);
    //#style mainCommand
    append("Salir",null);
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