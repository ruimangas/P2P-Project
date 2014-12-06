package main.java.pt.ist.gossip;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loggingService {


    public static void log(String numberOfNodes, String numberOfItems) throws IOException {

        try
        {
            String filename= "logs/gossipLogs";
            String filename2 = "logs/itemsLogs";
            if(Integer.parseInt(numberOfItems)!=0) {
                FileWriter fw2 = new FileWriter(filename2, true);
                fw2.write(numberOfItems+"\n");
                fw2.close();
            }

            if(Integer.parseInt(numberOfNodes)!=0) {

                FileWriter fw = new FileWriter(filename, true);
                fw.write(numberOfNodes + "\n");
                fw.close();
            }
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }
}





