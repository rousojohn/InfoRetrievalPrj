package Core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Word {
    private String word;
    private int timesSeenGenerally;
    private int postingPosition;
    private java.util.SortedMap<Integer,WordMap> map;
    private java.util.Vector<Double> weights;
    
    public Word(String word, int id, long pos, boolean _inTitle, int countEntries, int feedId){
        this.word = word;
        this.timesSeenGenerally=1;
        this.weights = new java.util.Vector<Double>();
        map = new java.util.TreeMap<Integer,WordMap>();
        map.put(id, new WordMap(id,pos,_inTitle,countEntries,feedId));
    }

    
    public void updateWord(int id, int pos){
        this.timesSeenGenerally++;
       // this.map.updatemap(id,pos);
    }
    
    
    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @return the timesSeenGenerally
     */
    public int getTimesSeenGenerally() {
        return timesSeenGenerally;
    }
    
     /**
     * @return the map
     */
    public java.util.SortedMap<Integer,WordMap> getMap() {
        return map;
    }

    /**
     * @param timesSeenGenerally the timesSeenGenerally to set
     */
    public void setTimesSeenGenerally(int timesSeenGenerally) {
        this.timesSeenGenerally = timesSeenGenerally;
    }
    
    
}
