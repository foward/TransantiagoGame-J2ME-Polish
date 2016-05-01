/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.utfsm.transchantiago.world;

import java.util.TimerTask;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Francisco
 */
/**
     * Animates a sprite.
     */
    public class SpriteAnimationTask extends TimerTask {

        private boolean moving = false;
        private boolean forward = true;
        private Sprite sprite;

        public SpriteAnimationTask(Sprite sprite, boolean forward) {
            this.sprite = sprite;
            this.forward = forward;
        }

        public void run() {
            if (!this.moving) {
                return;
            }
            if (this.forward) {
                System.out.println("nextFrame");
                this.sprite.nextFrame();
            } 
        }

        public void forward() {
             System.out.println("foward");
            this.forward = true;
            this.moving = true;
        }

        public void backward() {
            this.forward = false;
            this.moving = true;
        }

        public void setMoving(boolean isMoving) {
           // System.out.println("setMoving");
            this.moving = isMoving;
            
        }
    }