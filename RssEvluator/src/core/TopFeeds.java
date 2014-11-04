/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFrame;

/**
 *
 * @author home
 */
public class TopFeeds {
    private static java.util.SortedMap<String,Word> vocabulary;
    public static void findTops() throws FileNotFoundException, IOException{
        java.io.File vocFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\VocabularyFile.txt");
       java.io.File postFile= new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\PostingFile.txt");
        java.util.Scanner scanner = null;
        java.io.RandomAccessFile postRaf=null;
        try {
            scanner = new java.util.Scanner(vocFile);
        } catch (FileNotFoundException ex) {
            System.err.println("File: \""+vocFile.getAbsolutePath()+"\" does not exist.\n");
            System.exit(1);
        }
         postRaf = new java.io.RandomAccessFile(postFile, "r");
         vocabulary = new java.util.TreeMap<String, Word>();
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            Word word = new Word(line.split(":")[0],Integer.parseInt(line.split(":")[1]),Float.parseFloat(line.split(":")[2]),Long.parseLong(line.split(":")[3]));
            java.util.ArrayList<Double> array = new java.util.ArrayList<Double>();
            postRaf.seek(word.getPosting());
            for(int i=0; i<word.getDf(); i++){
                String postLine = postRaf.readLine();
                word.setMetro_weights(word.getMetro_weights()+Math.pow(Double.parseDouble(postLine.split("\t")[2]), 2));
              //  array.add(Double.parseDouble(postLine.split("\t")[2]));
            }
            word.setMetro_weights(Math.sqrt(word.getMetro_weights()));
            //System.out.println("----------> "+word.getWord());
            vocabulary.put(word.getWord().toLowerCase(), word);
        }
        scanner.close();
        postRaf.close();
        
         java.util.ArrayList<Word> array = new java.util.ArrayList<Word>();
         java.util.Iterator vocabIt = vocabulary.entrySet().iterator();
         while(vocabIt.hasNext()){
            Object o = vocabIt.next();
            Word word = (Word)((java.util.Map.Entry) o).getValue();
            array.add(word);
         }
         
         quickSort(array,0,array.size()-1);
        
         
         gui.result1 result = new gui.result1();
         for(int i=0; i<10; i++){
             result.addCategories(array.get(0).getWord());
             array.remove(0);
         }
         result.getFrame().pack();
         result.getFrame().setVisible(true);
    }
    
    public static void findTopFeeds(String w, gui.result1 frame) throws FileNotFoundException, IOException, IllegalArgumentException, FeedException{
         java.io.File docFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\DocumentsFile.txt");
       java.io.File postFile= new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\PostingFile.txt");
       
        RandomAccessFile postRaf = new java.io.RandomAccessFile(postFile, "r");
        RandomAccessFile docRaf = new java.io.RandomAccessFile(docFile,"r");
       
       Word word = vocabulary.get(w.toLowerCase());       
       postRaf.seek(word.getPosting());
       
       for(int i=0; i<word.getDf(); i++){
           String line = postRaf.readLine();
           int entry = Integer.parseInt(line.split("\t")[3]);
           long docPos = Long.parseLong(line.split("\t")[5]);
           int feedId = Integer.parseInt(line.split("\t")[6]);
           
           docRaf.seek(docPos);
           XmlReader reader = null;
            
            try {
                  reader = new XmlReader(new java.io.File(docRaf.readLine().split("\t")[1]));
            } catch (IOException ex) {
                System.err.println("Couldn't read XML.");
            }
             SyndFeed feed = new SyndFeedInput().build(reader);
             SyndEntry en = (SyndEntry) feed.getEntries().get(entry);
             frame.addLabel(en.getTitle(),en.getLink());
            reader.close();
             
       }
       
       postRaf.close();
       docRaf.close();
       
        
    }
    
     public static void quickSort(java.util.ArrayList<Word> arr, int left, int right) {
      int index = partition(arr, left, right);
      if (left < index - 1)
            quickSort(arr, left, index - 1);
      if (index < right)
            quickSort(arr, index, right);
}
    
private static int partition(java.util.ArrayList<Word> arr, int left, int right){
      int i = left, j = right;
      Word tmp;
      java.util.Vector<Double[]> tmp1;
      
      double pivot = arr.get((left + right) / 2).getMetro_weights();
      while (i <= j) {
            while (arr.get(i).getMetro_weights() < pivot)
                  i++;
            while (arr.get(j).getMetro_weights() > pivot)
                  j--;
            if (i <= j) {
                  tmp = arr.get(i);
                  arr.set(i,arr.get(j));
                  arr.set(j, tmp);
                  i++;
                  j--;
            }
      }
      return i;

}
    
}
