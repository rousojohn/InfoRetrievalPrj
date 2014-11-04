/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssindexer;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author xristina
 */
public class RssIndexer {

    /**
     * @param args the command line arguments
     */
    public static void main1(java.io.File args) throws IOException, IllegalArgumentException, FeedException {
              // URL url = new URL("http://rss.cnn.com/rss/edition.rss");
        XmlReader reader = null;
        try {
            reader = new XmlReader(args);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();
                String title = entry.getTitle();
                String link = entry.getLink();
                String desc = entry.getDescription().getValue();
                System.out.println("title: " + title + "\n" +
                                "link: " + link + "\n" +
                                "description: " + desc);
                List<SyndCategory> authors = entry.getAuthors();
                if ( (authors != null) && (!authors.isEmpty())) {
                    ListIterator it = authors.listIterator();
                    System.out.print("authors: ");
                    while (it.hasNext()) {
                        System.out.print(it.next() + ", ");
                    }
                    System.out.println();
                } else {
                    String author = entry.getAuthor();
                    System.out.println("author: " + author);
                }
                List<SyndCategory> categList = entry.getCategories();
                if ( (categList != null) && (!categList.isEmpty()) ) {
                    Iterator it = categList.listIterator();
                    System.out.print("Categories: ");
                    while (it.hasNext()) {
                        //System.out.println(it.next().toString());
                        SyndCategory tmp = (SyndCategory) it.next();
                        System.out.print(tmp.getName() + ", ");
                    }
                    System.out.println();
                }         
                System.out.println("----------------------");
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        
        
    }
    
    
    
     public static void tokenize(String filename) throws IOException, IllegalArgumentException, FeedException{
          java.io.File file = new java.io.File(filename);
          System.out.println("-----------> "+filename+"\n");
           if(file.isFile())
               main1(file);
               //if(file.getAbsolutePath().endsWith(".xml"))
                 //  this.tokenizeXMLFile(file);
                //else
                  // this.tokenizeFile(file.getAbsolutePath());
           else
               for(int i=0; i<file.listFiles().length; i++)
                   tokenize(file.listFiles()[i].getAbsolutePath());
        }

     
     public static void main(String[] args) throws IOException, IllegalArgumentException, FeedException{
         tokenize("C:\\Users\\xristina\\Desktop\\hy463_Tests\\RssFeedsCollection\\");
     }
     
     
    
}

