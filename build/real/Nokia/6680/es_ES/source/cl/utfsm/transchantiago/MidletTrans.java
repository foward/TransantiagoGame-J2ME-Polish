package cl.utfsm.transchantiago;

import cl.utfsm.transchantiago.util.RMS;
import cl.utfsm.transchantiago.util.Data;
import cl.utfsm.transchantiago.util.SplashScreen;
import cl.utfsm.transchantiago.GameScreen;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.CommandListener;  					import javax.microedition.lcdui.AlertType; 					import javax.microedition.lcdui.Canvas; 					import javax.microedition.lcdui.Command; 					import javax.microedition.lcdui.Display; 					import javax.microedition.lcdui.Displayable; 					import javax.microedition.lcdui.Font; 					import javax.microedition.lcdui.Graphics; 					import javax.microedition.lcdui.Image;  					import de.enough.polish.ui.*;
//#if polish.api.nokia-ui
import com.nokia.mid.ui.DeviceControl;
import com.nokia.mid.sound.Sound;
//#elif polish.midp2
//# import javax.microedition.lcdui.Display;
//#endif

/**
 * @author Francisco
 */
public class MidletTrans extends MIDlet {
      protected Display display;
  private Image splashLogo;
  private boolean isSplash = true;

  private MainMenuScreen mainMenuScreen;
  private GameScreen gameScreen;
  private SettingsScreen settingsScreen;
  private HighScoreScreen highScoreScreen;
  private HelpScreen helpScreen;
  private AboutScreen aboutScreen;

  private Alert alert;

  private static final boolean CONST_DEBUG = true;

  // Controlador de alamacenamiento persistente
  public RMS baseDatos;

  // Intancia con los datos de la aplicacion
  public Data datos;
  
  // Variables temporales (son utilizadas durante el juego)
  int puntajeActual;
  
  public Sound sonido;
  
  public MidletTrans() {
  }

  public void startApp() {
de.enough.polish.ui.StyleSheet.display = javax.microedition.lcdui.Display.getDisplay( this ); de.enough.polish.ui.StyleSheet.midlet = this;    display = Display.getDisplay(this);
    if(isSplash) {
      isSplash = false;
      try {

        // se cargan los datos desde el rms
        this.cargarDatos();
          
        mainMenuScreen = new MainMenuScreen(this);
        settingsScreen = new SettingsScreen(this);
        highScoreScreen = new HighScoreScreen(this);
        helpScreen = new HelpScreen(this);
        aboutScreen = new AboutScreen(this);

        splashLogo = Image.createImage("/splash.png");
        new SplashScreen(display, mainMenuScreen, splashLogo,3000);
      } catch(Exception ex) {
        showErrorMsg(null);
      }
    } else {
      mainMenuScreenShow(null);
    }

  }

  public Display getDisplay() {
    return display;
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    System.gc();
    notifyDestroyed();
  }

  protected static Image createImage(String filename) {
    Image image = null;
    try {
      image = Image.createImage(filename);
    } catch (Exception e) {
    }
    return image;
  }

  protected void mainMenuScreenShow(Alert alert) {
    if (alert==null)
      display.setCurrent(mainMenuScreen);
    else
      Alert.setCurrent( display, alert,mainMenuScreen );
  }
  
  protected void gameScreenShow() {
    try { 	
      gameScreen = null;
      gameScreen = new GameScreen(this);      
      gameScreen.start();
      display.setCurrent(gameScreen);
    } catch (Exception e) {
    	System.out.println(e);
      showErrorMsg(null);
    }  
  }  

  protected void settingsScreenShow() {
    try {
      settingsScreen.init();
      display.setCurrent(settingsScreen);
    } catch (Exception e) {
      showErrorMsg(null);
    }

  }

  protected void highScoreScreenShow() {
    try {
      highScoreScreen.init();
      display.setCurrent(highScoreScreen);
    } catch (Exception e) {
      showErrorMsg(null);
    }
  }

  protected void helpScreenShow() {
    display.setCurrent(helpScreen);
  }

  protected void aboutScreenShow() {
    display.setCurrent(aboutScreen);
  }

  protected void mainMenuScreenQuit() {
    destroyApp(true);
  }

  protected void showErrorMsg(String alertMsg) {
    if (alertMsg == null || CONST_DEBUG == false) {
      alertMsg = "Error Starting Elminator, may or may not function correctly.  Please contact support.";
    }
    alert = new Alert("Eliminator ERROR",alertMsg,null,null);
    alert.setTimeout(Alert.FOREVER);
    alert.setType(AlertType.ERROR);
    this.mainMenuScreenShow(alert);
  }   
  
    // Funcion que verifica si existen una base de datos creada
    private boolean hayDatos(){
            baseDatos = new RMS();
            if(baseDatos.hayRegistro()){
                    return true;
            }else{
                    return false;
            }
    }

    // Funcion para cargar la base de datos
    void cargarDatos(){
            this.datos = new Data();
            // Si existe una base de datos, los datos son cargados
            if(hayDatos()){
                    datos.setVolumen(Integer.parseInt(baseDatos.verRegistro(1)));
                    datos.setVibracion(Integer.parseInt(baseDatos.verRegistro(2)));
                    datos.setDificultad(Integer.parseInt(baseDatos.verRegistro(3)));
                    datos.setNombre1(baseDatos.verRegistro(4));
                    datos.setPuntaje1(Integer.parseInt(baseDatos.verRegistro(5)));
                    datos.setNombre2(baseDatos.verRegistro(6));
                    datos.setPuntaje2(Integer.parseInt(baseDatos.verRegistro(7)));
                    datos.setNombre3(baseDatos.verRegistro(8));
                    datos.setPuntaje3(Integer.parseInt(baseDatos.verRegistro(9)));
                    datos.setNombre4(baseDatos.verRegistro(10));
                    datos.setPuntaje4(Integer.parseInt(baseDatos.verRegistro(11)));
                    datos.setNombre5(baseDatos.verRegistro(12));
                    datos.setPuntaje5(Integer.parseInt(baseDatos.verRegistro(13)));
            } else {
                    // Si no existe una base de datos se crean valores por defecto
                    this.datos.setVolumen(0);
                    this.datos.setVibracion(1);
                    this.datos.setDificultad(1);
                    this.datos.setNombre1("AAA");
                    this.datos.setPuntaje1(0);
                    this.datos.setNombre2("BBB");
                    this.datos.setPuntaje2(0);
                    this.datos.setNombre3("CCC");
                    this.datos.setPuntaje3(0);
                    this.datos.setNombre4("DDD");
                    this.datos.setPuntaje4(0);
                    this.datos.setNombre5("EEE");
                    this.datos.setPuntaje5(0);
            }
    }

    // Funcion para guardar la base de datos
    void guardarDatos(Data datos){
            // Se eliminan los datos actuales de la base de datos
            baseDatos.eliminaBD();
            // Se guardan los datos actualizados
            baseDatos.guardaRegistro(String.valueOf(datos.getVolumen()));
            baseDatos.guardaRegistro(String.valueOf(datos.getVibracion()));
            baseDatos.guardaRegistro(String.valueOf(datos.getDificultad()));
            baseDatos.guardaRegistro(datos.getNombre1());
            baseDatos.guardaRegistro(String.valueOf(datos.getPuntaje1()));
            baseDatos.guardaRegistro(datos.getNombre2());
            baseDatos.guardaRegistro(String.valueOf(datos.getPuntaje2()));
            baseDatos.guardaRegistro(datos.getNombre3());
            baseDatos.guardaRegistro(String.valueOf(datos.getPuntaje3()));
            baseDatos.guardaRegistro(datos.getNombre4());
            baseDatos.guardaRegistro(String.valueOf(datos.getPuntaje4()));
            baseDatos.guardaRegistro(datos.getNombre5());
            baseDatos.guardaRegistro(String.valueOf(datos.getPuntaje5()));
    }
    
  //metodo que se encarga de hacer vibrar el equipo
	public void vibrar(){
		if(this.datos.getVibracion() == 1){
			//#if polish.api.nokia-ui
			try {
				 DeviceControl.startVibra(100,200);
			} catch (IllegalStateException e) {
				System.out.println("Device does not support vibration" + e );
			}
			//#elif polish.midp2
			//# Display.getDisplay(this).vibrate(200);
			//#endif
		}else{
			System.out.println("vibrar desactivado");
		}
	}
	
	public void flashLight(){
		//#if polish.api.nokia-ui
		System.out.println("Flash Ligth");
		DeviceControl.flashLights(2000);
		//#endif
	}
	
	public void backLight(int luz){
		//#if polish.api.nokia-ui
		System.out.println("Luz: " + luz + "%");
		DeviceControl.setLights(0, luz);
		//#endif
	}
	
	public void playTone(int frecuencia, int tiempo){
		//#if polish.api.nokia-ui
		Sound s = new Sound(frecuencia, tiempo);
		s.play(1);
		//#endif
	}
}
