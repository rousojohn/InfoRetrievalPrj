package Core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Document {
    private int id;
    private String path;
    private String type;
    private long positionInDocFile;
    private int maxFreq;
    
    public Document(int id, String filename){
        java.io.File file = new java.io.File(filename);
        if(!file.exists()){
            System.err.println("PUBLIC DOCUMENT\n");
            System.err.println("File: \""+filename+"\" does not exist.");
            System.exit(1);
        }
        this.id=id;
        this.path=new String(filename);
        this.type=getTypeOfFile(file);
        this.maxFreq =1;
    }
    
    
     private String getTypeOfFile(java.io.File file){
           String s = new StringBuffer(file.getAbsolutePath()).reverse().toString();
           String k="";
           for( int o=0; o<s.length(); o++){
               if(s.charAt(o)!='.')
                   k+=s.charAt(o);
               else
                   break;
           }
           return (new StringBuffer(k).reverse().toString());
        }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the positionInDocFile
     */
    public long getPositionInDocFile() {
        return positionInDocFile;
    }

    /**
     * @param positionInDocFile the positionInDocFile to set
     */
    public void setPositionInDocFile(long positionInDocFile) {
        this.positionInDocFile = positionInDocFile;
    }

    /**
     * @return the maxFreq
     */
    public int getMaxFreq() {
        return maxFreq;
    }

    /**
     * @param maxFreq the maxFreq to set
     */
    public void setMaxFreq(int maxFreq) {
        this.maxFreq = maxFreq;
    }
    
}
