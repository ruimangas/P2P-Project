package main.java.pt.ist.gossip;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loggingService {


    public static void log(String numberOfNodes, Date data) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try
        {
            String filename= "logs/gossipLogs";
            FileWriter fw = new FileWriter(filename,true);
            fw.write("\nNUMBER OF NODES: " + numberOfNodes + " AT TIME: " + sdf.format(data)+ "\n");
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }
}





