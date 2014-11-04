package CoreTxt;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class WordMap {
    private int documentId;
    private java.util.ArrayList<Long>positions;

    public WordMap(int id, long pos){
        this.documentId = id;
        this.positions = new java.util.ArrayList<Long>();
        this.positions.add(pos);
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
    
    
    
}
