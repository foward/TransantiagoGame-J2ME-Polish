/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.world;

/**
 *
 * @author Francisco
 */
import java.util.TimerTask;
import javax.microedition.lcdui.CommandListener;  					import javax.microedition.lcdui.AlertType; 					import javax.microedition.lcdui.Canvas; 					import javax.microedition.lcdui.Command; 					import javax.microedition.lcdui.Display; 					import javax.microedition.lcdui.Displayable; 					import javax.microedition.lcdui.Font; 					import javax.microedition.lcdui.Graphics; 					import javax.microedition.lcdui.Image;  					import de.enough.polish.ui.*;
import javax.microedition.lcdui.game.*;

/**
 * Main Sprite / Player
 * Inherits the MIDP 2.0 Sprite class
 */
public class PlayerSprite extends Sprite {
  private static final int MOVE = 3;  
  private int x,y;
  private int scnWidth,scnHeight;
  private int frameWidth, frameHeight;
  private int frame;
  private int lives;
  private int dineroPorPeaton=0;


public int[] leftSequence = {0, 0};
  public int[] rightSequence = {2, 2};
  private int widthTerrain=0;



  public PlayerSprite(Image image, int frameWidth, int frameHeight, int scnWidth, int scnHeight, int widthTerrain) throws Exception {
    super(image, frameWidth, frameHeight);
    x = frameWidth/2;
    y = frameHeight/2;
    this.setFrame(1);
    this.scnWidth = scnWidth;
    this.scnHeight = scnHeight;
    this.frameWidth = frameWidth;
    this.frameHeight = frameHeight;
    this.widthTerrain = widthTerrain;
    this.frame = 1;
    this.lives = 3;    
  }

  public void startPosition() {
    setPosition((scnWidth-frameWidth)/2,scnHeight/2);
  }

  public void moveLeft() {
    getXY();
    if(x - MOVE >0)
        move(MOVE * -1,0);
              
  }

  public void moveRight() {
    getXY();
   // if (x + MOVE + frameWidth < scnWidth)
    //poner ancho del terreno
    if(this.widthTerrain - frameWidth >= x)
        move(MOVE,0);
  
 
  }

  public void moveUp() {
    getXY();
    if (y - MOVE > 0)      
      move(0,MOVE * -1);
  }

  public void moveUpAcelerated(int TURBO) {
    getXY();
    //TODO cambiar 5 a TUrbo
    if (y - 5 > 0)      
      move(0,5 * -1);
  }
  public void moveDown() {
    getXY();
    this.dineroPorPeaton= 1500;
    System.out.println("y"+y);
    if (y + MOVE + frameHeight < scnHeight)      
      move(0,MOVE);
  }

  public void setDineroPorPeaton(int dineroPorPeaton) {
		this.dineroPorPeaton = dineroPorPeaton;
	}
  
  public void display(Graphics g) {
    this.setFrame(frame);
    this.paint(g);
  }
  
  public int getLives() {
    return lives;
  }

  public void setLives(int lives) {
    this.lives = lives;
  }

  private void getXY() {
    x = getX();
    y = getY();
  }
  
  public int getMOVE(){
    return this.MOVE;
  }

public int getDineroRecollected() {
	return this.dineroPorPeaton;
}
  
  
  
}
