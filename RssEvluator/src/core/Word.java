/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;


public class Word {
    private String word;
    private int df;
    private double idf;
    private long posting;
    private java.util.ArrayList<Double> weights;
    private double metro_weights;
    
    public Word(String word,int times,float idf, long posting ){
        this.word = word;
        this.df = times;
        this.posting = posting;
        this.idf = idf;
        metro_weights=0;
    }
    
    public Word(String word,int times){
        this.df = times;
        this.word = word;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @return the timesSeen
     */
    public int getDf() {
        return df;
    }

    /**
     * @return the posting
     */
    public long getPosting() {
        return posting;
    }

    /**
     * @return the idf
     */
    public double getIdf() {
        return idf;
    }

    /**
     * @param df the df to set
     */
    public void setDf(int df) {
        this.df = df;
    }

    /**
     * @param idf the idf to set
     */
    public void setIdf(float idf) {
        this.idf = idf;
    }

    /**
     * @return the weights
     */
    public java.util.ArrayList<Double> getWeights() {
        return weights;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(java.util.ArrayList<Double> weights) {
        this.weights = weights;
    }

    /**
     * @return the metro_weights
     */
    public double getMetro_weights() {
        return metro_weights;
    }

    /**
     * @param metro_weights the metro_weights to set
     */
    public void setMetro_weights(double metro_weights) {
        this.metro_weights = metro_weights;
    }
    
}
