/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author jrobertson1
 */
public class BImage extends JComponent{

    /**
     * @param args the command line arguments
     */
    
    public void paint(Graphics g) {
   int blk = Color.BLACK.getRGB();
   int wht = Color.WHITE.getRGB();
   // The letter A (very small)
   int data[][]= {
       {wht,wht,blk,wht,wht},
       {wht,blk,wht,blk,wht},
       {wht,blk,blk,blk,wht},
       {wht,blk,wht,blk,wht},
       {wht,blk,wht,blk,wht}};    
         
     BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
    for (int x=0;x<5;x++) {
        for (int y=0; y<5; y++) {                                   
              image.setRGB(x,y,data[x][y]);
         }
    }
   
    g.drawImage(image, 0, 0, this);
  }
     public static void main(String[] args) {
    JFrame frame = new JFrame("BufferedImage A");
    frame.getContentPane().add(new BImage());
    frame.setSize(300, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
    
}
