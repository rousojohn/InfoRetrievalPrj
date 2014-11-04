package Core;


import com.sun.syndication.io.FeedException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Project {
    private static java.util.SortedMap<String,Word> vocabulary;
    private static java.util.ArrayList<Document> documentList;
    
    
    
    public static void main(String[] args){
        args = new String[2];
        args[0]=System.getProperties().getProperty("user.home");
        java.io.File file = new java.io.File(args[0]+"\\CollectionIndex\\DocumentsFile.txt");
        if(file.exists())
            file.delete();
         file = new java.io.File(args[0]+"\\CollectionIndex\\VocabularyFile.txt");
        if(file.exists())
            file.delete();
        file  = new java.io.File(args[0]+"\\CollectionIndex\\PostingFile.txt");
         if(file.exists())
            file.delete();
        file = new java.io.File(args[0]+"\\CollectionIndex");
        if(file.exists())
            file.delete();
        gui.Indexer.main1(args,true);
    }
    
    
    
    
    public static void main2(String[] args) throws IOException, IllegalArgumentException, FeedException{
        vocabulary = new java.util.TreeMap<String,Word>();
        documentList = new java.util.ArrayList<Document>();
        
        MyTokenizer tokenizer = new MyTokenizer(args[0]);
        
        
        tokenizer.tokenize(args[1]);
        
      //  calculateWeights();
        
        java.io.File file = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex");
        if(!file.exists())
            file.mkdir();
        
        file = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\DocumentsFile.txt");
        java.io.RandomAccessFile raf = null;
        
        try {
            raf = new java.io.RandomAccessFile(file,"rw");
        } catch (FileNotFoundException ex) {
            System.out.println("MY_TOKENIZER---1\n");
            System.err.println("File: \""+args[1]+"\" does not exists.\n");
        }
        
        raf.writeBytes(documentList.size()+"\n");
            for(int i=0; i<documentList.size(); i++){
                documentList.get(i).setPositionInDocFile(raf.getFilePointer());
                raf.writeUTF(String.valueOf(documentList.get(i).getId())+"\t"+documentList.get(i).getPath()+"\t"+documentList.get(i).getType()+"\n");
            }
        
            
            
            
        try {
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        file = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\VocabularyFile.txt");
        java.io.File post = new java.io.File(System.getProperties().getProperty("user.home")+"\\CollectionIndex\\PostingFile.txt");
        
        FileOutputStream fos=null;
        java.nio.charset.Charset charset = java.nio.charset.Charset.forName("utf-8");
        java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
			new FileOutputStream(file), charset));
        
        
        try {
            
            raf = new java.io.RandomAccessFile(post,"rw");
        } catch (FileNotFoundException ex) {
             System.out.println("MY_TOKENIZER---2\n");
        }
        raf.seek(0);
        
        java.util.Iterator<Entry<String,Word>> it = vocabulary.entrySet().iterator();
     
        int i=1;
        while(it.hasNext()){
            
            Object o = it.next();
           Word w = (Word) ((Entry)o).getValue();
           java.util.SortedMap map =  w.getMap();
           java.util.Iterator<Entry<Integer,WordMap>> mapIt = map.entrySet().iterator();
           
           long postingPos = raf.getFilePointer();
           
         int df = w.getMap().size();
         
         double idf =  Math.log( documentList.size()/df ) / Math.log( 2 );
         
            out.append(w.getWord()+":"+df+":"+idf+":"+postingPos+"\n");
            
            
           while(mapIt.hasNext()){
               Object ob=mapIt.next();
               WordMap wm = (WordMap)((Entry)ob).getValue();
               
               double tf = ((double)wm.getPositions().size() / documentList.get(wm.getDocumentId()-1).getMaxFreq() );
               double weight = tf*idf;
               if(wm.isInTitle())
                   weight *= 0.65;
               else
                   weight *= 0.35;
               
               raf.writeBytes(String.valueOf(wm.getDocumentId())+"\t"
                       +tf+":"
                       +wm.positionsToString()
                       +String.valueOf(weight)+"\t"
                       + String.valueOf(wm.getInEntry())+"\t"
                       + (wm.isInTitle()?"1":"0")+"\t"
                       + documentList.get(wm.getDocumentId()-1).getPositionInDocFile()+"\t"
                       + String.valueOf(wm.getFeedId())+"\n");
           }
           
           
        }
        raf.close();
        out.close();
        
        
    }
    
    
    
//    
//    private static void calculateWeights(){
//        java.util.Iterator it =  
//        
//        
//        int df = w.getMap().size();
//         double idf =  Math.log( documentList.size()/df ) / Math.log( 2 );
//          while(mapIt.hasNext()){
//               Object ob=mapIt.next();
//               WordMap wm = (WordMap)((Entry)ob).getValue();
//               
//               double tf = ((double)wm.getPositions().size() / documentList.get(wm.getDocumentId()-1).getMaxFreq() );
//               
//               raf.writeBytes(String.valueOf(wm.getDocumentId())+"\t"+tf+":"+wm.positionsToString()+
//                       documentList.get(wm.getDocumentId()-1).getPositionInDocFile()+"\n");
//           }
//    }
    
    public static java.util.SortedMap<String, Word> getVocabulary(){
        return vocabulary;
    }
    
    public static java.util.ArrayList<Document> getDocumentList(){
        return documentList;
    }
}
