/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.world;

/**
 *
 * @author Francisco
 */
import javax.microedition.lcdui.CommandListener;  					import javax.microedition.lcdui.AlertType; 					import javax.microedition.lcdui.Canvas; 					import javax.microedition.lcdui.Command; 					import javax.microedition.lcdui.Display; 					import javax.microedition.lcdui.Displayable; 					import javax.microedition.lcdui.Font; 					import javax.microedition.lcdui.Graphics; 					import javax.microedition.lcdui.Image;  					import de.enough.polish.ui.*;
import javax.microedition.lcdui.game.*;

public class GameMap {

public final static boolean DEBUG = true;

  // MAP 1:
  //TODO: change that for a binary file to improve the performance.
private static final short[][] map1 = {
			{ 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 0, 20, 20, 20, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 0, 20, 20, 20, 20, 20, 20, 0, 0, 0, 20, 20, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 0, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 0, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 5, 5, 5, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 0, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 20, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 20, 20, 0, 0, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 0, 0, 20, 20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
              };

// Map 2
private static final short[][] map2 = {
             { 28, 28, 23, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 121, 122, 123, 124 },
              { 28, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 126, 127, 128, 129 },
              { 119, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 81, 82, 83, 84 },
              { 124, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 86, 87, 88, 89 },
              { 129, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 91, 92, 93, 94 },
              { 134, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 96, 97, 98, 99 },
              { 28, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 3, 101, 102, 103, 104 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 3, 106, 107, 108, 109 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 3, 111, 112, 113, 114 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 0, 1, 24, 28, 28 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 0, 0, 0, 0, 7, 28 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 4, 0, 0, 0, 0, 0, 0, 0, 3, 28 },
              { 15, 21, 0, 0, 0, 0, 0, 0, 2, 2, 9, 28, 21, 0, 0, 0, 0, 0, 0, 0, 3, 28 },
              { 15, 28, 8, 0, 0, 0, 0, 3, 116, 117, 118, 119, 28, 8, 2, 0, 0, 0, 0, 0, 3, 28 },
              { 15, 15, 15, 4, 0, 0, 0, 3, 121, 122, 31, 32, 33, 34, 35, 4, 0, 0, 0, 0, 3, 28 },
              { 15, 15, 15, 4, 0, 0, 0, 3, 126, 127, 36, 37, 38, 39, 40, 4, 0, 0, 0, 0, 3, 28 },
              { 15, 15, 15, 4, 0, 0, 0, 3, 131, 132, 41, 42, 38, 44, 45, 4, 0, 0, 0, 0, 3, 28 },
              { 15, 15, 15, 4, 0, 0, 0, 0, 24, 28, 46, 47, 48, 49, 50, 4, 0, 0, 0, 0, 10, 28 },
              { 15, 15, 15, 21, 0, 0, 0, 0, 0, 7, 51, 52, 53, 54, 55, 4, 0, 0, 0, 0, 3, 28 },
              { 81, 82, 83, 84, 8, 0, 0, 0, 0, 3, 56, 57, 58, 59, 60, 4, 0, 0, 0, 0, 3, 28 },
              { 86, 87, 88, 89, 15, 4, 0, 0, 0, 3, 61, 62, 63, 64, 65, 4, 0, 0, 0, 0, 3, 28 },
              { 91, 92, 93, 94, 15, 4, 0, 0, 0, 3, 66, 67, 68, 69, 70, 4, 0, 0, 0, 0, 3, 28 },
              { 96, 97, 98, 99, 15, 4, 0, 0, 0, 3, 71, 72, 73, 74, 75, 4, 0, 0, 0, 0, 3, 28 },
              { 101, 102, 103, 104, 15, 4, 0, 0, 0, 3, 76, 77, 78, 79, 80, 4, 0, 0, 0, 0, 3, 28 },
              { 106, 107, 108, 109, 15, 4, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 3, 28 },
              { 111, 112, 113, 114, 15, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 28 },
              { 28, 28, 28, 28, 28, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 28 },
              { 28, 28, 28, 28, 28, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 28 },
              { 41, 42, 43, 44, 45, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 9, 28, 28 },
              { 46, 47, 48, 49, 50, 4, 0, 0, 0, 3, 4, 0, 0, 0, 3, 81, 82, 83, 84, 28, 28, 28 },
              { 46, 47, 48, 49, 50, 4, 0, 0, 0, 3, 4, 0, 0, 0, 3, 86, 87, 88, 89, 15, 15, 15 },
              { 51, 52, 53, 54, 55, 4, 0, 0, 0, 3, 4, 0, 0, 0, 3, 91, 92, 93, 94, 15, 15, 15 },
              { 56, 57, 58, 59, 60, 4, 0, 0, 0, 3, 4, 0, 0, 0, 3, 31, 32, 33, 34, 35, 15, 15 },
              { 61, 62, 63, 64, 65, 4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 36, 37, 38, 39, 40, 15, 15 },
              { 66, 67, 68, 69, 70, 4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 41, 42, 43, 44, 45, 15, 15 },
              { 71, 72, 73, 74, 75, 4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 46, 47, 48, 49, 50, 15, 15 },
              { 76, 77, 78, 79, 80, 4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 51, 52, 53, 54, 55, 15, 15 },
              { 28, 23, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 56, 57, 58, 59, 60, 15, 15 },
              { 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 61, 62, 63, 64, 65, 15, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 0, 3, 66, 67, 68, 69, 70, 15, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 0, 0, 22, 4, 0, 0, 0, 3, 71, 72, 73, 74, 75, 15, 15 },
              { 4, 0, 0, 0, 0, 0, 2, 2, 9, 28, 4, 0, 0, 0, 3, 76, 77, 78, 79, 80, 15, 15 },
              { 4, 0, 0, 0, 0, 3, 116, 117, 118, 119, 4, 0, 0, 0, 3, 15, 15, 15, 15, 15, 15, 15 },
              { 4, 0, 0, 0, 0, 3, 121, 122, 123, 124, 4, 0, 0, 0, 3, 116, 117, 118, 119, 28, 28, 15 },
              { 4, 0, 0, 0, 0, 3, 126, 127, 128, 129, 4, 0, 0, 0, 3, 121, 122, 123, 124, 28, 28, 15 },
              { 4, 0, 0, 0, 0, 3, 131, 132, 133, 134, 4, 0, 0, 0, 3, 126, 127, 128, 129, 28, 28, 15 },
              { 4, 0, 0, 0, 0, 3, 15, 15, 15, 15, 4, 0, 0, 0, 3, 131, 132, 133, 134, 28, 28, 15 },
              { 4, 0, 0, 0, 0, 0, 24, 28, 28, 23, 0, 0, 0, 0, 3, 28, 28, 28, 28, 28, 28, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 7, 6, 0, 0, 0, 0, 0, 3, 15, 15, 15, 15, 15, 15, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 15, 15, 116, 117, 118, 119, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 15, 15, 121, 122, 123, 124, 15 },
              { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 15, 15, 126, 127, 128, 129, 15 },
              { 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 15, 15, 131, 132, 133, 134, 15 },
              { 28, 8, 2, 2, 0, 0, 0, 0, 0, 0, 2, 2, 2, 9, 28, 15, 15, 15, 15, 15, 15, 15 },
              { 81, 82, 83, 84, 4, 0, 0, 0, 0, 3, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28 },
              { 86, 87, 88, 89, 4, 0, 0, 0, 0, 3, 31, 32, 33, 34, 35, 28, 28, 28, 28, 28, 28, 28 },
              { 96, 97, 98, 99, 4, 0, 0, 0, 0, 3, 36, 37, 38, 39, 40, 28, 15, 15, 15, 15, 15, 15 },
              { 101, 102, 103, 104, 4, 0, 0, 0, 0, 10, 41, 42, 43, 44, 45, 28, 15, 0, 0, 0, 0, 0 },
              { 101, 102, 103, 104, 4, 0, 0, 0, 0, 3, 46, 47, 48, 49, 50, 28, 15, 0, 0, 0, 0, 0 },
              { 106, 107, 108, 109, 4, 0, 0, 0, 0, 3, 51, 81, 82, 83, 84, 28, 15, 0, 0, 0, 0, 0 },
              { 111, 112, 113, 114, 4, 0, 0, 0, 0, 3, 56, 86, 87, 88, 89, 28, 15, 0, 0, 0, 0, 0 },
              { 28, 28, 23, 1, 0, 0, 0, 0, 0, 3, 61, 91, 92, 93, 94, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 6, 0, 0, 0, 0, 0, 0, 0, 3, 66, 96, 97, 98, 99, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 3, 71, 101, 102, 103, 104, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 0, 0, 0, 0, 22, 76, 106, 107, 108, 109, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 0, 2, 2, 9, 28, 28, 111, 112, 113, 114, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 31, 32, 33, 34, 35, 28, 28, 28, 28, 28, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 36, 37, 38, 39, 40, 28, 15, 15, 15, 15, 15, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 41, 42, 43, 44, 45, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 46, 47, 48, 49, 50, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 51, 52, 53, 54, 55, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 56, 57, 58, 59, 60, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 61, 62, 63, 64, 65, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 66, 67, 68, 69, 70, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 71, 72, 73, 74, 75, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 76, 77, 78, 79, 80, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 28, 28, 28, 28, 28, 28, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
              { 15, 4, 0, 0, 0, 3, 15, 15, 15, 15, 15, 15, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
			};

//mapa3
private static final short[][] map3 = {
	{ 0, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0 },
    { 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 26, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 26, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 26, 0, 0, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 29, 0, 16, 17, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 13, 0, 0, 0, 0, 0, 0, 0, 16, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 14, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 18, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 17, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
              };


  // Set Constant values, the values are direct
  // relation to the actually tile sizes
  // defined for the terrain graphics
  private static final int TILE_WIDTH = 32;
  private static final int TILE_HEIGHT = 32;
  private static final int TILE_NUM_COL = 22;
  private static final int TILE_NUM_ROW = 78;

  // To hold the current map
  private short[][] currentMap;
  private short[][] edificiosMap;
  private short[][] hoyosMap;

  // To hold the current terrain
  private TiledLayer terrain;
  private TiledLayer Edificios;
  private TiledLayer hoyos;

  // To hold the current background/floor color
  private int groundColor;
  
  // To hold the current screen, value neeeded for scrolling calculation
  private int screenHeight,screenWidth;
  
  // To hold Y position for scrolling
  private int terrainScrollY;  
  
  private int SPEED_MAP =3;
  
  //To hold X position for scrolling
  private int terrainScrollX;
  private boolean isStop =false;
  private boolean isAcelerate = false;


  public GameMap(int screenHeight) throws Exception {
    this.screenHeight = screenHeight;
    this.screenWidth = screenWidth;
    //setMap(2);  // default to set to terrain 1
    setMap(2);
  }

  // Set Appropriate Terrain and Map
  public void setMap(int level) throws Exception {
    Image tileImages = null;

    switch (level) {
      case 1:  tileImages = Image.createImage("/tiles.png");
               currentMap = map1;
               groundColor = 0x373737;
               break;
      case 2:  tileImages = Image.createImage("/tiles.png");
               currentMap = map1;
               edificiosMap = map2;
               hoyosMap = map3;
               groundColor = 0xDECE6B;
               break;
      default: tileImages = Image.createImage("/TILES-MAPA-8-sn.png");
               currentMap = map1;
               groundColor = 0xF8DDBE;
               break;
    }

    terrain = new TiledLayer(TILE_NUM_COL,TILE_NUM_ROW,tileImages,TILE_WIDTH,TILE_HEIGHT);
    Edificios = new TiledLayer(TILE_NUM_COL,TILE_NUM_ROW,tileImages,TILE_WIDTH,TILE_HEIGHT);
    hoyos = new TiledLayer(TILE_NUM_COL,TILE_NUM_ROW,tileImages,TILE_WIDTH,TILE_HEIGHT);
    // Map Terrain Map with actual graphic from terrain.png
    for (int row=0; row<TILE_NUM_ROW; row++) {
      for (int col=0; col<TILE_NUM_COL; col++) {
      	terrain.setCell(col,row,currentMap[row][col]);
        Edificios.setCell(col,row,edificiosMap[row][col]);
        hoyos.setCell(col,row,hoyosMap[row][col]);
      }
    }
    
    if(DEBUG){
        System.out.println("cellHeight :"+terrain.getCellHeight());
        System.out.println("getRows: "+terrain.getRows());
        System.out.println("screenHeight: "+screenHeight);
        System.out.println("terrainWidth: "+terrain.getWidth());
        System.out.println("terrainHeight: "+terrain.getHeight());
    }

    terrainScrollY = 1 - (terrain.getCellHeight() * terrain.getRows()) + screenHeight;
    terrainScrollX = 1 - (terrain.getCellWidth() * terrain.getColumns()) + screenWidth;
    terrain.setPosition(0,terrainScrollY);    
    Edificios.setPosition(0,terrainScrollY);
    hoyos.setPosition(0,terrainScrollY);
  }
 
  public void scrollTerrain() {
	    if (terrainScrollY < 0 && !this.isStop) {
	        if(this.isAcelerate){
	           
	            this.SPEED_MAP = 6;
	             System.out.println("Acelerate ..."+this.SPEED_MAP);
	        }else{
	            this.SPEED_MAP = 3;
	        }
	        
	        
	      terrainScrollY += this.SPEED_MAP;
	      terrain.setPosition(0,terrainScrollY);
	      Edificios.setPosition(0,terrainScrollY);
	      hoyos.setPosition(0,terrainScrollY);
	    }
	     
	  }  
  
  public void setisStop(boolean isStop){
      this.isStop=isStop;
  }
  
   public boolean getisStop(){
      return this.isStop;
  }
   
   public void setisAcelerate(boolean isAcelerate){
      this.isAcelerate=isAcelerate;
  }
  
   public boolean getisAcelerate(){
      return this.isAcelerate;
  }

  // Terrain Getter
  public TiledLayer getTerrain() {
    return terrain;
  }
  
  public TiledLayer getEdificios(){
      return Edificios;
  }
  
  public TiledLayer getHoyos(){
      return hoyos;
  }

  // Ground/Floor color Getter
  public int getGroundColor() {
    return groundColor;
  }    
}