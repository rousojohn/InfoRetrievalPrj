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


public class Queryevaluator {

    private static java.util.SortedMap<String,Word> vocabulary;
    private static java.util.HashMap<Integer,java.util.Vector<Double[]>> documentsVectors;
    /**
     * @param args the command line arguments
     */
    public static void main1(String args1) throws IOException, IllegalArgumentException, FeedException {
       java.io.File vocFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\VocabularyFile.txt");
       java.io.File docFile = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\DocumentsFile.txt");
       java.io.File postFile= new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\PostingFile.txt");
       java.io.RandomAccessFile postRaf=null, docRaf=null; 
       java.util.Scanner scanner = null;
       
       MyTokenizer tokenizer = new MyTokenizer();
       String[] args = tokenizer.tokenizeQuery(args1);
       
       java.util.HashMap<String,Word> query = eliminateDuplicates(args);
     
       
       java.util.Vector<Double> queryVector;
        
        try {
            scanner = new java.util.Scanner(vocFile);
        } catch (FileNotFoundException ex) {
            System.err.println("File: \""+vocFile.getAbsolutePath()+"\" does not exist.\n");
            System.exit(1);
        }
        
        
        postRaf = new java.io.RandomAccessFile(postFile, "r");
        docRaf = new java.io.RandomAccessFile(docFile,"r");
        
        
        
        documentsVectors = new java.util.HashMap<Integer, java.util.Vector<Double[]>>();
        queryVector = new java.util.Vector<Double>();
        vocabulary = new java.util.TreeMap<String, Word>();
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            Word word = new Word(line.split(":")[0],Integer.parseInt(line.split(":")[1]),Float.parseFloat(line.split(":")[2]),Long.parseLong(line.split(":")[3]));
            vocabulary.put(word.getWord().toLowerCase(), word);
            queryVector.add(0.0);
        }
        scanner.close();
       
        
        
        
        
        java.util.Vector<Double> R= new java.util.Vector<Double>();

        
        
        
        java.util.Iterator vocabIt = vocabulary.entrySet().iterator();
        int counter=0; 
        while(vocabIt.hasNext()){
            Object o = vocabIt.next();
            Word word = (Word)((java.util.Map.Entry) o).getValue();
            postRaf.seek(word.getPosting());
            
            for(int k=0; k<word.getDf(); k++){
                String line = postRaf.readLine();
                int docId = Integer.parseInt(line.split("\t")[0]);
               // double tf = Double.parseDouble(line.split("\t")[1].split(":")[0]);
                
                double weight = Double.parseDouble(line.split("\t")[2]);
                int entry = Integer.parseInt(line.split("\t")[3]);
                
                
                
               //int inTitle = Integer.parseInt(line.split("\t")[4]);
                long docPos = Long.parseLong(line.split("\t")[5]);
                int feedId = Integer.parseInt(line.split("\t")[6]);

                if(!documentsVectors.containsKey(docId-1)){
                    java.util.Vector<Double[]> v = new java.util.Vector<Double[]>();
                    for(int j=0;j<vocabulary.size(); j++){
                        Double pin[]=new Double[3];
                       pin[0]=0.0;
                       pin[1]=0.0;
                       pin[2]=0.0;
                       v.add(pin);
                    }
                       Double pin[]=new Double[3];
                       pin[0]=weight;
                       pin[1]=(double)docPos;
                       pin[2]=(double)entry;
                    v.set(counter, pin);
                    documentsVectors.put(docId-1, v);
                }
                else{
                     Double pin[]=new Double[3];
                     pin[0]=weight;
                     pin[1]=(double)docPos;
                     pin[2]=(double)entry;
                     documentsVectors.get(docId-1).set(counter, pin);
                }
            }
            if(query.containsKey(word.getWord().toLowerCase()))
                queryVector.set(counter,query.get(word.getWord().toLowerCase()).getIdf() * word.getIdf());
            
            counter++;
        }
        
        
        postRaf.close();
        
       
        for(int i=0;i<documentsVectors.size();i++)
        {
             double ari8mitis=0,paronomastis=0, par1=0,par2=0;
            if(documentsVectors.get(i)==null) System.out.println("EISAI MALAKAS\n");
            for(int j=0;j<documentsVectors.get(i).size(); j++){
                if(documentsVectors.get(i).get(j)!=null){
                    ari8mitis+= (documentsVectors.get(i).get(j)[0] * queryVector.get(j));
                    par1+=Math.pow(documentsVectors.get(i).get(j)[0], 2);
                    par2+=Math.pow(queryVector.get(j), 2);
                }
            }
            paronomastis = par1*par2;
            R.add(ari8mitis/Math.sqrt(paronomastis));
        }
        quickSort(R,0,R.size()-1);
       // int howmany = 0;
        
        gui.result1 resultWin =new gui.result1();
        for(int i=R.size()-1; i>=0; i--){
            if(R.get(i) == 0.0)
                break;
            long pos = getDocPos(documentsVectors.get(i));
            docRaf.seek(pos);
            XmlReader reader = null;
            
            try {
                  reader = new XmlReader(new java.io.File(docRaf.readLine().split("\t")[1]));
            } catch (IOException ex) {
                System.err.println("Couldn't read XML.");
            }
             SyndFeed feed = new SyndFeedInput().build(reader);
             int entry = getEntry(documentsVectors.get(i));
             SyndEntry en = (SyndEntry) feed.getEntries().get(entry);
            resultWin.addLabel(en.getTitle(),en.getLink());
            reader.close();
        }
        resultWin.getFrame().pack();
        resultWin.getFrame().setVisible(true);
        
        
    }
    
    private static int getEntry(java.util.Vector<Double[]> v){
        for(int i=0; i<v.size(); i++){
            if(v.get(i)[0] != 0.0)
                return (int)v.get(i)[2].doubleValue();
        }
        return -1;
    }
    
    private static long getDocPos(java.util.Vector<Double[]> v){
        for(int i=0; i<v.size(); i++){
            if(v.get(i)[0] != 0.0)
                return (long)v.get(i)[1].doubleValue();
        }
        return -1;        
    }
    
    
    public static void quickSort(java.util.Vector<Double> arr, int left, int right) {
      int index = partition(arr, left, right);
      if (left < index - 1)
            quickSort(arr, left, index - 1);
      if (index < right)
            quickSort(arr, index, right);
}
    
private static int partition(java.util.Vector<Double> arr, int left, int right){
      int i = left, j = right;
      double tmp;
      java.util.Vector<Double[]> tmp1;
      
      double pivot = arr.get((left + right) / 2);
      while (i <= j) {
            while (arr.get(i) < pivot)
                  i++;
            while (arr.get(j) > pivot)
                  j--;
            if (i <= j) {
                  tmp = arr.get(i);
                  arr.set(i,arr.get(j));
                  arr.set(j, tmp);
                  tmp1 = documentsVectors.get(i);
                  documentsVectors.put(i, documentsVectors.get(j));
                  documentsVectors.put(j, tmp1);
                  i++;
                  j--;
            }
      }
      return i;

}
    
    
    
    
    public static java.util.HashMap<String,Word> eliminateDuplicates(String[] args){
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
    
    
    
    
    
}
