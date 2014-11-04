/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.FileNotFoundException;


public class MyTokenizer {
     private java.util.ArrayList<String> stopWords;
    public MyTokenizer(){
        stopWords = new java.util.ArrayList<String>();
         java.util.Scanner scanner=null;
        try {
            scanner = new java.util.Scanner(new java.io.File(System.getProperties().getProperty("user.home")+"\\StopWords.txt"));
        } catch (FileNotFoundException ex) {
            System.err.println("File: \""+System.getProperties().getProperty("user.home")+"\\StopWords.txt"+"\" does not exists.\n");
            System.exit(1);
        }
                while(scanner.hasNext())
                    this.stopWords.add(scanner.nextLine());
    }
    
    public String[] tokenizeQuery(String str){
        String delimiter = " \t\n\r\f~`!@#$%^&*()_-+={}[]:;'|,.?0123456789/\"?\\><";
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(str, delimiter);
        java.util.ArrayList<String> toReturn = new java.util.ArrayList<String>();
        
        while(tokens.hasMoreTokens()){
            String token = tokens.nextToken();
            if(!this.stopWords.contains(token))
                toReturn.add(token);
        }
        String[] array = new String[toReturn.size()];
        
        System.out.println("=================================== ");
        for(int i=0; i<array.length; i++){
            array[i]=toReturn.get(i);
            System.out.println(array[i]);
        }
        System.out.println("=================================== ");
        
        return array;
//        return (String[]) toReturn.toArray();    
    }
    
}
