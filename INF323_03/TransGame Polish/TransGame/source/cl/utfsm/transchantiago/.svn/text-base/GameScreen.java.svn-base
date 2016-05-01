/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago;

/**
 *
 * @author eraddatz
 */
import cl.utfsm.transchantiago.world.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

public class GameScreen extends GameCanvas implements Runnable, CommandListener {
    
  public final static boolean DEBUG = true;
  private static final int MILLIS_PER_TICK = 20;
  private int SPEED = 0;
  private static final int MIN_BUFFER = 10;
  private int viewPortX = 0;
  private int viewPortY = 0;
  private byte lastDirection = -1;
  private MidletTrans midlet;  // Hold the Main Midlet
  private Command backCommand = new Command("Volver", Command.BACK,1);
  private GameMap gameMap;
  private SpriteAnimationTask spriteMicroAnimator;

  private boolean isPlay;   // Game Loop runs when isPlay is true
  private int width;        // To hold screen width
  private int height;       // To hold screen height

  private int scnViewWidth; // Hold Width Screen View Port
  private int scnViewHeight; // Hold Height Screen View Port

  private Thread gameThread = null;

  // Layer Manager to manager background (terrain)
  private LayerManager layerManager;

  // TiledLayer - Terrain
  private TiledLayer terrain;
  private TiledLayer edificios;
  private TiledLayer hoyos;
  private int terrainScroll;   // Hold Y position for scrolling
  
  // Sprites
  private PlayerSprite player;  
    
  private int topeIzqMicro =0;
  private int topeDerMicro =0;
  private Timer timer;
  private int widthTerrain;
  private boolean isMicroAcelerating =false;
  private int TURBO =5;
  private boolean noKeyPressed = true;

  

  // Constructor and initialization
  public GameScreen(MidletTrans midlet) throws Exception {
    super(true);
    setFullScreenMode(true);
    this.midlet = midlet;
    addCommand(backCommand);
    setCommandListener(this);

    width = getWidth();  // get screen width
    height = getHeight();  // get screen height
    scnViewWidth = width; // Set View Port width to screen width
    scnViewHeight = height; // Set View Port height to screen height
   
    this.timer = new Timer();   
    isPlay = true;   

    // setup map
    gameMap = new GameMap(scnViewHeight);
    terrain = gameMap.getTerrain();
    edificios = gameMap.getEdificios();
    hoyos = gameMap.getHoyos();
    widthTerrain = terrain.getWidth();
    
    // setup player sprite
    Image image = Image.createImage("/micro16-tile2.png");
    player = new PlayerSprite (image,36,53,width,height,widthTerrain);  // 36 = width of sprite in pixels, 53 is height of sprite in pixels
    player.startPosition();
    player.defineReferencePixel(18, 27);
    spriteMicroAnimator = new SpriteAnimationTask(this.player, false);
    this.timer.scheduleAtFixedRate(this.spriteMicroAnimator, 0, 150);
    SPEED = player.getMOVE();
    
    //variables tope
    this.topeIzqMicro = ( this.getWidth() - this.player.getWidth() ) /2;
    this.topeDerMicro = ( this.terrain.getWidth() - this.topeIzqMicro - this.player.getWidth());

  
    
    layerManager = new LayerManager();
    layerManager.append(player);    
    layerManager.append(terrain);
    layerManager.append(edificios);
    layerManager.append(hoyos);

  }

  // Start thread for game loop
  public void start() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  // Stop thread for game loop
  public void stop() {
    gameThread = null;
  }

  // Main Game Loop
  public void run() {
    Graphics g = getGraphics();

    Thread currentThread = Thread.currentThread();

    try {
      while (currentThread == gameThread) {
      	long startTime = System.currentTimeMillis();
      	if (isShown()) {
      	  if (isPlay) {
            // Scroll Terrain	
            gameMap.scrollTerrain();   
       
            if(this.player.collidesWith(this.hoyos,true)){
            	this.midlet.vibrar();
            }
            
            // Player Actions 
            int keyStates = getKeyStates();
            boolean isMicroStop = this.gameMap.getisStop();
            this.isMicroAcelerating = this.gameMap.getisAcelerate();
             //if user is pressing the left button
              if ((keyStates & LEFT_PRESSED) != 0) {
                        //if the previous direction was other than left set the sequence
                        //correct sequence & transform needed for moving to the left
                    if(!isMicroStop)
                        this.player.setFrame(0);
                        if (this.lastDirection != LEFT) {
                            this.lastDirection = LEFT;
                          
                            continue;
                        }
                        //this.spriteMicroAnimator.forward();

                        //if moving the sprite generates a collision return sprite back
                        //to its original position
                      //  if (this.spriteCollides(this.player)) {
                         //   this.player.moveRight();
                            //continue;
                     //   }
                        //attempt to adjust the viewport to keep the sprite on the screen
                        if(this.viewPortX == 0 && this.player.getX() < this.topeIzqMicro ){
                            if(!isMicroStop)
                                this.player.moveLeft();
                        }else if (this.viewPortX >= ((this.terrain.getWidth() - this.getWidth())) && this.player.getX() >=  (this.topeDerMicro - 3)){ 
                            if(!isMicroStop)
                                this.player.moveLeft();

                        }else{
                            if(!isMicroStop){
                                this.player.moveLeft();
                                this.adjustViewport(this.viewPortX, this.viewPortY);
                            }
                        }


                    } else if ((keyStates & RIGHT_PRESSED) != 0) {
                        
                       if(!isMicroStop)
                            this.player.setFrame(2);
                       
                        if (this.lastDirection != RIGHT) {
                            this.lastDirection = RIGHT;
                          
                            continue;
                        }
                       // this.spriteMicroAnimator.forward();

                       // if (this.spriteCollides(this.player)) {
                         //   this.player.moveLeft();
                            //continue;
                       // }

                       // this.adjustViewport(this.viewPortX + SPEED, this.viewPortY);
                        if(this.viewPortX == 0 && this.player.getX() < this.topeIzqMicro ){
                            if(!isMicroStop)
                                 this.player.moveRight();
                        }else if (this.viewPortX >= (this.terrain.getWidth() - this.getWidth()) && this.player.getX() >=  this.topeDerMicro -3){ 
                            if(!isMicroStop)
                                this.player.moveRight();

                        }else{
                            if(!isMicroStop){
                                this.player.moveRight();
                                this.adjustViewport(this.viewPortX, this.viewPortY);
                            }
                        }

                    } else if ((keyStates & UP_PRESSED) != 0) {
                        if (this.lastDirection != UP) {
                            this.lastDirection = UP;
                            continue;

                        }
                                          
                        this.gameMap.setisStop(false);
                        this.gameMap.setisAcelerate(true);
                       // this.player.moveUp();
                        
                        this.player.moveUpAcelerated(TURBO);

                        this.adjustViewport(this.viewPortX, this.viewPortY);

                    } else if ((keyStates & DOWN_PRESSED) != 0) {
                        if (this.lastDirection != DOWN) {
                            this.lastDirection = DOWN;
                            continue;
                        }
// La primera vez : es freno .....
                        // mantener apretado es retroceder
                        if(!noKeyPressed){
                            System.out.println("Keypressed y boolean: "+ noKeyPressed);
                            this.player.moveDown();
                            this.adjustViewport(this.viewPortX, this.viewPortY);
                        }else{
                            System.out.println("else lean: "+ noKeyPressed);
                             this.gameMap.setisStop(true);
                        }
                        
                       
                        
                    } else {
                        this.noKeyPressed = true;
                        this.SPEED = player.getMOVE();
                        this.player.setFrame(1);
                        this.gameMap.setisAcelerate(false);
                    }


            // Player Fires
            if ((keyStates & FIRE_PRESSED) != 0) {
              //TODO poner sonido bocina
            }

      	  } // END WHILE
          render(g);
      	}
      	long timeTake = System.currentTimeMillis() - startTime;
      	if (timeTake < MILLIS_PER_TICK) {
      	  synchronized (this) {
      	    wait(MILLIS_PER_TICK - timeTake);
      	  }
      	} else {
      	  currentThread.yield();
      	}
      }
    } catch (InterruptedException ex) {
      // won't be thrown
    }

  }
/**
     * Adjust the viewport to keep the main animated sprite inside the screen.
     * The coordinates are checked for game bounaries and adjusted only if it
     * makes sense.
     *
     * @param x viewport X coordinate
     * @param y viewport Y coordinate
     */
    private void adjustViewport(int xViewportNow, int yViewportNow) {
        int xmax = terrain.getWidth() - this.getWidth();
        int ymax = 0;
        int ymin = terrain.getHeight() - this.getHeight();
        int xmin = 0;
        
        if(DEBUG){
           // System.out.println("viewport X " + this.viewPortX);              
        } 
         if (xViewportNow <= xmin && xViewportNow >= xmax) {
            return;
        }
        
        if (this.lastDirection == LEFT && this.viewPortX > xmin){
                    this.viewPortX -=this.SPEED;
        }
        if (this.lastDirection == RIGHT && this.viewPortX <= xmax) {
                    this.viewPortX +=this.SPEED;
        }
        if (this.lastDirection == DOWN && this.viewPortY < ymin){
             this.viewPortY +=this.SPEED;
        }
        if (this.lastDirection == UP && this.viewPortY >= ymax){
            if( this.isMicroAcelerating){
                this.SPEED = this.TURBO;
            }
             this.viewPortY -=this.SPEED;
        }
          System.out.println("Speed: "+this.SPEED); 
        //adjust the viewport
        this.layerManager.setViewWindow(this.viewPortX, this.viewPortY, this.getWidth(), this.getHeight());
    }



  public void commandAction(Command c, Displayable d) {
    if (c == backCommand) {
      midlet.mainMenuScreenShow(null);
    }
  }

  // Method to Display Graphics
  private void render(Graphics g) {

    // Set Background color to beige
    //g.setColor(0xF8DDBE);
    g.setColor(gameMap.getGroundColor());
    g.fillRect(0,0,width,height);
    g.setColor(0x0000ff);

    // Get Current Map 
    terrain = gameMap.getTerrain();

    // LayerManager Paint Graphics
    layerManager.paint(g,0,0);

    flushGraphics();
  }  
}
