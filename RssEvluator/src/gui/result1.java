/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.sun.syndication.io.FeedException;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author xristina
 */
public class result1{
  //  private final JLabel jLabel1;
    private JFrame frame=null;
    
   
    public result1(){
      // super("Result");
        frame = new JFrame("Result");
        frame.setLayout(new GridLayout(0, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // jLabel1=new JLabel("sdfsdfs");
      //  add(jLabel1);
        frame.setSize(400, 300);
        

    }
    
    public void addCategories(String category){
         final result1 tmp =this;
        JButton b = new JButton(category);
        b.setBorderPainted(false);
        b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               JLabel label=new JLabel(((JButton)evt.getSource()).getText());
                getFrame().remove((JButton)evt.getSource());
                getFrame().add(label);
                try {
                    core.TopFeeds.findTopFeeds(label.getText(), tmp);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FeedException ex) {
                    Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
       // this = tmp;
        getFrame().add(b);
        getFrame().validate();
        getFrame().repaint();
    }
    
    
    public void addLabel(String str,final String link){
        JButton b = new JButton(str);
        b.setBorderPainted(false);
        b.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    try {
                        buttonActionPerformed(link);
                    } catch (IOException ex) {
                        Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(result1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
        });
        getFrame().add(b);
        getFrame().validate();
        getFrame().repaint();
    }
    
 
  private void buttonActionPerformed(String string) throws URISyntaxException, IOException {
      final URI uri = new URI(string);
      if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(uri);
      } catch (IOException e) { /* TODO: error handling */ }
    }
  }

    /**
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }
}
