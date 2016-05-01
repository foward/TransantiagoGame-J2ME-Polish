/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago;

/**
 *
 * @author Francisco
 */
import cl.utfsm.transchantiago.util.Score;
import javax.microedition.lcdui.*;

public class HighScoreScreen extends Form implements CommandListener {
  private MidletTrans midlet;
  private Command backCommand = new Command("Volver", Command.BACK, 1);
  private Command resetCommand = new Command("Resetear", Command.SCREEN,1);
  private Score score;
  StringItem stringItem;

  public HighScoreScreen (MidletTrans midlet,Score score) throws Exception {
    super("Ranking");
    this.midlet = midlet;
    this.score = score;
    stringItem = new StringItem(null,"");
    append(stringItem);
    addCommand(backCommand);
    addCommand(resetCommand);
    setCommandListener(this);
  }

  public void commandAction(Command c, Displayable d) {
    if (c == backCommand) {
      midlet.mainMenuScreenShow(null);
      return;
    }
    if (c == resetCommand) {
      processMenu();
    }
  }

  public void init() throws Exception {
    score.loadScores();
    stringItem.setText(buildHighScore());
  }

  private void processMenu() {
    try {
      score.reset();      
      midlet.mainMenuScreenShow(null);
    }  catch (Exception ex) {
      midlet.showErrorMsg("null");
    }
  }

  private String buildHighScore() {
    String result = "";
    String[] names = score.getNames();
    int[] values = score.getValues();
    for (int i=0; i<names.length; i++) {
      result = result + names[i] + "   " + values[i] + "\n";
    }
    return result;
  }
}