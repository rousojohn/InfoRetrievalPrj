/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Desktop;
import java.awt.GridLayout;
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
public class result extends JFrame{
  //  private final JLabel jLabel1;
    
   
    public result(){
       super("Result");
        setLayout(new GridLayout(0, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // jLabel1=new JLabel("sdfsdfs");
      //  add(jLabel1);
        setSize(400, 300);

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
                        Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
        });
        this.add(b);
        this.validate();
        this.repaint();
    }
    
    
    
  private void buttonActionPerformed(String string) throws URISyntaxException, IOException {
      final URI uri = new URI(string);
      if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(uri);
      } catch (IOException e) { /* TODO: error handling */ }
    }
  }
}
