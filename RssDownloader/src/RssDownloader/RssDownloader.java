/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RssDownloader;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author xristina
 */
public class RssDownloader {
    public static void main(String[] args) throws MalformedURLException, IOException, IllegalArgumentException, FeedException{
        if(args.length<1){
            System.err.println("You didn't give the Rss links as argument.");
            System.exit(0);
        }
        for(int i=0; i<args.length; i++){
            java.net.URL url = new java.net.URL(args[i]);
            XmlReader reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, new java.io.File(args[i].split("/")[args[i].split("/").length-1].replace("rss", "xml")));
            reader.close();
        }
    }
    
}
