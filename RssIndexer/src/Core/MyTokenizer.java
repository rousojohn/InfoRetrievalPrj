package Core;


import java.util.Iterator;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.StringTokenizer;
import static java.nio.file.StandardCopyOption.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class MyTokenizer {

  //  private int wordPosition;
    private int file_id;
    private int feed_id;
    private java.util.ArrayList<String> stopWords;
    
    public MyTokenizer(String filename) throws IOException{
        try {
                file_id=0;
                feed_id=0;
                stopWords = new java.util.ArrayList<String>();
                java.io.File file = new java.io.File(filename);
                java.nio.file.Path path = FileSystems.getDefault().getPath(System.getProperties().getProperty("user.home")+"\\StopWords.txt");
              
                Files.copy(FileSystems.getDefault().getPath(file.getPath()), path,REPLACE_EXISTING);


                java.util.Scanner scanner = new java.util.Scanner(file);
                while(scanner.hasNext())
                    this.stopWords.add(scanner.nextLine());
                    
        } catch (FileNotFoundException ex) {
            System.err.println("File: \""+filename+"\" does not exists.\n");
            System.exit(1);
        }
    }
    
    
    
      public void tokenize(String filename) throws IOException, IllegalArgumentException, FeedException{
          java.io.File file = new java.io.File(filename);
          System.out.println("-----------> "+filename+"\n");
           if(file.isFile())
               if(file.getAbsolutePath().endsWith(".xml"))
                   this.tokenizeXMLFile(file);
               else ;
               //    this.tokenizeFile(file.getAbsolutePath());
           else
               for(int i=0; i<file.listFiles().length; i++)
                   tokenize(file.listFiles()[i].getAbsolutePath());
        }
    
      
      
      private void tokenizeXMLFile(java.io.File file) throws IOException, IllegalArgumentException, FeedException{
          long wordPosition=0L;
          this.file_id++;
          String delimiter = " \t\n\r\f~`!@#$%^&*()_-+={}[]:;'|,.?0123456789/\"?\\";
          Project.getDocumentList().add(new Document(file_id,file.getAbsolutePath()));
          XmlReader reader = null;
        try {
            reader = new XmlReader(file);
        } catch (IOException ex) {
           System.err.println("Couldn't read XML.");
        }
        
        SyndFeed feed = new SyndFeedInput().build(reader);
        int countEntries=0;
        for(Iterator i=feed.getEntries().iterator(); i.hasNext();){
            SyndEntry entry = (SyndEntry) i.next();
            String title = entry.getTitle();
            String desc = entry.getDescription().getValue();
          //  String link = entry.getLink();
            StringTokenizer tokens = new StringTokenizer(title, delimiter);
            while(tokens.hasMoreTokens()){
                String currentToken = new String(tokens.nextToken().getBytes(),"UTF-8");
                if(!this.stopWords.contains(currentToken.toLowerCase())){
                    if(Project.getVocabulary().containsKey(currentToken.toLowerCase()))
                        updateVocabulary(currentToken.toLowerCase(), this.file_id, wordPosition,true,countEntries);
                    else
                        Project.getVocabulary().put(currentToken.toLowerCase(),new Word(currentToken,this.file_id,wordPosition,true,countEntries,feed_id));
                }
            }
            
            tokens = new StringTokenizer(desc, delimiter);
            while(tokens.hasMoreTokens()){
                String currentToken = new String(tokens.nextToken().getBytes(),"UTF-8");
                if(!this.stopWords.contains(currentToken.toLowerCase())){
                    if(Project.getVocabulary().containsKey(currentToken.toLowerCase()))
                        updateVocabulary(currentToken.toLowerCase(), this.file_id, wordPosition,false,countEntries);
                    else
                        Project.getVocabulary().put(currentToken.toLowerCase(),new Word(currentToken,this.file_id,wordPosition,false,countEntries,feed_id));
                }
            }
            
            countEntries++;   
            feed_id++;
        }

        
        reader.close();
          
          
          
          
      }
    
 
   
   private void updateVocabulary(String word, int id, long pos,boolean _inTitle,int countEntries){
       Word w = Project.getVocabulary().get(word);
       w.setTimesSeenGenerally(w.getTimesSeenGenerally() + 1);
       if(w.getMap().containsKey(id)){
           w.getMap().get(id).getPositions().add(pos);
           if(Project.getDocumentList().get(id-1).getMaxFreq() < w.getMap().get(id).getPositions().size())
               Project.getDocumentList().get(id-1).setMaxFreq(w.getMap().get(id).getPositions().size());
       }
       else
           w.getMap().put(id, new WordMap(id,pos,_inTitle,countEntries,feed_id));
       
       
   }
   
   
   
    
}
