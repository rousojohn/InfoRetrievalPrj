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


public class Ocapi {
 private static java.util.SortedMap<String,Word> vocabulary;
 private static java.util.HashMap<Integer,Integer> totalWordsInADocument;
 //private static java.util.
 public static void run(String args1) throws FileNotFoundException, IOException, IllegalArgumentException, FeedException{
     // long start = System.nanoTime();
       java.io.File vocFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\VocabularyFile.txt");
       java.io.File docFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\DocumentsFile.txt");
       java.io.File postFile= new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\PostingFile.txt");
       java.io.RandomAccessFile postRaf=null, docRaf=null; 
       java.util.Scanner scanner = null;
       
       MyTokenizer tokenizer = new MyTokenizer();
       String[] args = tokenizer.tokenizeQuery(args1);
       
       java.util.HashMap<String,Word> query = eliminateDuplicates(args);
       
        try {
            scanner = new java.util.Scanner(vocFile);
        } catch (FileNotFoundException ex) {
            System.err.println("File: \""+vocFile.getAbsolutePath()+"\" does not exist.\n");
            System.exit(1);
        }
        
        
        postRaf = new java.io.RandomAccessFile(postFile, "r");
        docRaf = new java.io.RandomAccessFile(docFile,"r");
        
        int collectionDocsSize = Integer.parseInt(docRaf.readLine());
        
        java.util.HashMap<Integer,Double[]> scores = new java.util.HashMap<Integer,Double[]>();
        for(int  i=0; i<collectionDocsSize; i++){
            Double[] pin = new Double[3];
            pin[0] = 0.0;
            pin[1] = -1.0;
            pin[2] = -1.0;
            scores.put(i, pin);
        }
        System.out.println(collectionDocsSize);
        
        
        vocabulary = new java.util.TreeMap<String, Word>();
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            Word word = new Word(line.split(":")[0],Integer.parseInt(line.split(":")[1]),Float.parseFloat(line.split(":")[2]),Long.parseLong(line.split(":")[3]));
            vocabulary.put(word.getWord().toLowerCase(), word);
        //    queryVector.add(0.0);
        }
        scanner.close();
        
        countTotalWordsOfDocuments(postRaf);
        double avgdl = calculateAVGDL();
        
        
        java.util.Iterator it = query.entrySet().iterator();
        
        
        double K1 = 2.0, b=0.75;
        int countWord=0;
       
        while(it.hasNext()){
            Object o = it.next();
            String word = ((Word) ((java.util.Map.Entry) o).getValue()).getWord();
            Word w = vocabulary.get(word.toLowerCase());
            //double score=0.0;
            if(w == null) continue;
            postRaf.seek(w.getPosting());
            
            double idf = Math.log((collectionDocsSize-w.getDf()+0.5) / (w.getDf() + 0.5));
            
            for(int i=0; i<w.getDf(); i++){
                String line = postRaf.readLine();
                int docId = Integer.parseInt(line.split("\t")[0]);
                
                long docPos = Long.parseLong(line.split("\t")[5]);
                //long textPos = Long.parseLong(line.split("\t")[1].split(":")[1].split(",")[0]);
                int entry = Integer.parseInt(line.split("\t")[3]);
                
                int FqiInD = line.split("\t")[1].split(":")[1].split(",").length;
               
                scores.get(docId-1)[0]+=(idf * ((FqiInD *(K1+1)) / (FqiInD+K1*(1-b+b*totalWordsInADocument.get(docId-1)/avgdl)) ));
                if(scores.get(docId-1)[1] == -1){
                    scores.get(docId-1)[1] = (double)docPos;
                    scores.get(docId-1)[2] = (double)entry;
                }
            }
            countWord++;
        }
        java.util.ArrayList<Double[]> scoresArray = new java.util.ArrayList<Double[]>();
        for(int i=0; i<collectionDocsSize; i++)
            scoresArray.add(scores.get(i));
        
        quickSort(scoresArray,0,scores.size()-1);
        
        java.io.RandomAccessFile textFile ;
        gui.result1 resultWin =new gui.result1();
        for(int i=scoresArray.size()-1; i>=0; i--){
            if(scoresArray.get(i)[0] == 0.0)
                break;
            long pos = scoresArray.get(i)[1].longValue();
            docRaf.seek(pos);
            XmlReader reader = null;
            
             try {
                  reader = new XmlReader(new java.io.File(docRaf.readLine().split("\t")[1]));
            } catch (IOException ex) {
                System.err.println("Couldn't read XML.");
            }
             SyndFeed feed = new SyndFeedInput().build(reader);
             int entry = scoresArray.get(i)[2].intValue();
             SyndEntry en = (SyndEntry) feed.getEntries().get(entry);
            resultWin.addLabel(en.getTitle(),en.getLink());
            reader.close();
        }
        resultWin.getFrame().pack();
        resultWin.getFrame().setVisible(true);
        
             
            
            /*
            String path = docRaf.readLine().split("\t")[1];
            textFile = new java.io.RandomAccessFile(new java.io.File(path),"r");
            textFile.seek(scoresArray.get(i)[2].longValue());
            String snippet = textFile.readLine();
           
            System.out.println("\t"+scoresArray.get(i)[0]+"\t"+path);
            System.out.println("\t\t\" "+snippet+" \".");
            textFile.close();
             * 
             */
        
        
        docRaf.close();
        postRaf.close();
    //    System.err.append("Finished in "+(System.nanoTime()-start)* 1.0e-9 +" .\n");
        
 }
 
 
 
 
 
 
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 ///////////////////////////////////////                HELPFUL FUNCTIONS         ////////////////////////////////////////////////////////
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
 
 
 
 
 private static java.util.HashMap<String,Word> eliminateDuplicates(String[] args){
        java.util.HashMap<String,Word> newTable = new java.util.HashMap<String,Word>();
        int maxFreq=1;
        for(int i=0; i<args.length; i++){
            Word w = new Word(args[i],1);
            if(newTable.containsKey(w.getWord().toLowerCase())){
                        newTable.get(w.getWord().toLowerCase()).setDf(newTable.get(w.getWord().toLowerCase()).getDf()+1);
                        if(maxFreq < newTable.get(w.getWord().toLowerCase()).getDf())
                            maxFreq = newTable.get(w.getWord().toLowerCase()).getDf();
            }
            else
                newTable.put(w.getWord().toLowerCase(), w);
        }
        
        java.util.Iterator it = newTable.entrySet().iterator();
        while(it.hasNext()){
            Object o = it.next();
            ((Word) ((java.util.Map.Entry)o).getValue()).setIdf((float)((Word) ((java.util.Map.Entry)o).getValue()).getDf()/maxFreq);            
        }
        
        
        
        
        return newTable;
    }
 
 
 private static void countTotalWordsOfDocuments(java.io.RandomAccessFile postRaf) throws IOException{
     totalWordsInADocument = new java.util.HashMap<Integer, Integer>();
     java.util.Iterator it = vocabulary.entrySet().iterator();
     while(it.hasNext()){
         //Object o = it.next();
         Word w = (Word)((java.util.Map.Entry)it.next()).getValue();
         postRaf.seek(w.getPosting());
         for(int i=0; i<w.getDf(); i++){
             String line = postRaf.readLine();
             int count=line.split("\t")[1].split(":")[1].split(",").length;
             int docId = Integer.parseInt(line.split("\t")[0]);
             
             if(totalWordsInADocument.containsKey(docId-1))
                 totalWordsInADocument.put(docId-1, totalWordsInADocument.get(docId-1)+count);
             else
                 totalWordsInADocument.put(docId-1, count);
         }
     }
 }
 
 
 
 private static double calculateAVGDL(){
     int sum=0;
     java.util.Iterator it = totalWordsInADocument.entrySet().iterator();
     while(it.hasNext()){
         sum += ((Integer) ((java.util.Map.Entry)it.next()).getValue()).intValue();
     }
     return (sum/totalWordsInADocument.size());
 }
 
 
   
    public static void quickSort(java.util.ArrayList<Double[]> arr, int left, int right) {
      int index = partition(arr, left, right);
      if (left < index - 1)
            quickSort(arr, left, index - 1);
      if (index < right)
            quickSort(arr, index, right);
}
    
private static int partition(java.util.ArrayList<Double[]> arr, int left, int right){
      int i = left, j = right;
      Double[] tmp;
      java.util.Vector<Double[]> tmp1;
      
      Double[] pivot = arr.get((left + right) / 2);
      while (i <= j) {
            while (arr.get(i)[0] < pivot[0])
                  i++;
            while (arr.get(j)[0] > pivot[0])
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
