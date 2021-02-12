
package tilegame;

import javax.swing.JFrame;
import java.awt.*;

public class TileGame {
 
    public static void main(String[] args) {
    	JFrame fr = new JFrame("The Doors");
	fr.setSize(1250,1000);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(0,0,0));
	Panel panel = new Panel();
	panel.setOpaque(false);
	fr.add(panel);
	fr.setVisible(true);
    }  
}

