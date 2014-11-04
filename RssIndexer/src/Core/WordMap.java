package Core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class WordMap {
    private int documentId;
    private java.util.ArrayList<Long>positions;
    private boolean inTitle;
    private int inEntry;
    private int feedId;

    public WordMap(int id, long pos, boolean _intitle, int entry,int _feedId){
        this.documentId = id;
        this.feedId=_feedId;
        this.positions = new java.util.ArrayList<Long>();
        this.positions.add(pos);
        this.inTitle=_intitle;
        this.inEntry=entry;        
    }
    
    public String positionsToString(){
        String str="";
        int i;
        for( i=0; i< this.positions.size()-1; i++)
            str += this.positions.get(i)+",";
        str+=this.positions.get(i)+"\t";
        return str;
    }
    /**
     * @return the documentId
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * @return the positions
     */
    public java.util.ArrayList<Long> getPositions() {
        return positions;
    }

    /**
     * @return the inTitle
     */
    public boolean isInTitle() {
        return inTitle;
    }

    /**
     * @return the inEntry
     */
    public int getInEntry() {
        return inEntry;
    }

    /**
     * @return the feedId
     */
    public int getFeedId() {
        return feedId;
    }
    
    
    
}
