/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.util;

import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;
import java.io.*;

/**
*
* @author eraddatz
*/
public class Sound {
	public Player p;
	public VolumeControl vc;


	public void stopSound(){
		try{
			p.stop();
		}catch(Exception e){
			System.out.println("error al detener sonido: " + e);
		}
	}
	
	public void volume(int vol){
		vc = (VolumeControl) p.getControl("VolumeControl");
		vc.setLevel(vol);
		System.out.println("Volumen: " + vol + "%");
	}
	
	public void playLoop(int loop){
		p.setLoopCount(loop);
		System.out.println("Loop: " + loop);
	}
	
    public void play(String file, String type){
    	if (file == null){
    		 System.out.println("error al reprodicir el sonido!!");
        }else{
            try{
            	InputStream is = getClass().getResourceAsStream(file);
                p = Manager.createPlayer(is, type);
                p.realize();
                p.start();
            }
            catch (IOException ioe){
                System.out.println("error!! IOException: " + ioe);
            }
            catch (MediaException me){
                System.out.println("error!! MediaException: " + me);
            }
            catch (SecurityException se){
                System.out.println("error!! SecurityException: " + se);
            }
        }
    }
}

