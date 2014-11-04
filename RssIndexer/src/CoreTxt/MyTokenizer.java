package CoreTxt;


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
    private java.util.ArrayList<String> stopWords;
    
    public MyTokenizer(String filename) throws IOException{
        try {
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
    
    
    
      public void tokenize(String filename){
          java.io.File file = new java.io.File(filename);
          System.out.println("-----------> "+filename+"\n");
           if(file.isFile())
               this.tokenizeFile(file.getAbsolutePath());
           else
               for(int i=0; i<file.listFiles().length; i++)
                   tokenize(file.listFiles()[i].getAbsolutePath());
        }
    
    
    
      /**
       * @param file 
       */
   public void tokenizeFile(String file){
           // this.wordPosition=0;
      
           long wordPosition=0;
            this.file_id++;
            Project.getDocumentList().add(new Document(file_id,file));
            java.io.BufferedReader reader = null;
           //java.io.RandomAccessFile reader = null;
            StringTokenizer tokenizer = null;
            String delimiter = " \t\n\r\f~`!@#$%^&*()_-+={}[]:;'|,.?0123456789/\"?\\"; // kovoume ta punct ektos ' kai -
            String line = null, currentToken = null;

            try {
                reader = new java.io.BufferedReader(new java.io.FileReader(file));
                while ((line = reader.readLine()) != null){
                    tokenizer = new StringTokenizer(line, delimiter);
                    while(tokenizer.hasMoreTokens() ) {
                        currentToken = new String(tokenizer.nextToken().getBytes(),"UTF-8");
                        
                        if(!this.stopWords.contains(currentToken.toLowerCase())){
                            if(Project.getVocabulary().containsKey(currentToken.toLowerCase()))
                                updateVocabulary(currentToken.toLowerCase(),this.file_id,wordPosition);
                            else
                                Project.getVocabulary().put(currentToken.toLowerCase(),new Word(currentToken, this.file_id, wordPosition) );
                        }
                     //  wordPosition+=currentToken.length()+1;
                    }
                    wordPosition += line.length()+1;
                }
            }catch (FileNotFoundException e) {
                System.err.println("File "+file+" not found.");
                System.exit(1);
            }
            catch (IOException e) {
                System.err.println("Error in reading file: " +file);
                System.exit(1);
            }
    }
    
   
   
   
   private void updateVocabulary(String word, int id, long pos){
       Word w = Project.getVocabulary().get(word);
       w.setTimesSeenGenerally(w.getTimesSeenGenerally() + 1);
       if(w.getMap().containsKey(id)){
           w.getMap().get(id).getPositions().add(pos);
           if(Project.getDocumentList().get(id-1).getMaxFreq() < w.getMap().get(id).getPositions().size())
               Project.getDocumentList().get(id-1).setMaxFreq(w.getMap().get(id).getPositions().size());
       }
       else
           w.getMap().put(id, new WordMap(id,pos));
       
       
   }
   
   
   
    
}
