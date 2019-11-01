package ceit.aut.ac.ir;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Date;

public class httpThread extends Thread {
    private Socket sSocket;


    public httpThread(Socket cSocket) {
        sSocket = cSocket;
    }

    public void run() {

        InputStreamReader isr = null; //read http request packets from clients
        PrintWriter out = null; // send http responses to clients
        String shortUrl = null;
        String longUrl = null;
        boolean longExist = false;


        try {
            isr = new InputStreamReader(sSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //a reader to read each line of the request
        BufferedReader reader = new BufferedReader(isr);
        try {
            // process the request line by line
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.isEmpty()) break; // Stop when headers are completed.
                System.out.println(line);

                if (line.contains("GET")) {
                    //find out the shorten url
                    shortUrl = line.substring(line.indexOf("GET /") + 5, line.indexOf(" HTTP"));
                    System.out.println(shortUrl);
                }

                if (line.contains("Cookie:")) { //check whether the cookie exists
                    if (line.contains(shortUrl)) { //check whether the cookie is valid
                        longExist = true;
                        longUrl = line.substring(line.indexOf("\t"));  // set longUrl from the cookie to increase speed
                        System.out.println("1: " + longUrl);

                    }
                }

            }
            if (!longExist) {
                //If cookie didn't exist or was not valid retrieve the longUrl from file(slow process)
                urlShortener shortenedUrl = new urlShortener();
                longUrl = shortenedUrl.retrieveUrl(shortUrl);
                System.out.println("2: " + longUrl);
            }

            // we get character output stream to client (for headers)
            out = new PrintWriter(new OutputStreamWriter(sSocket.getOutputStream()));
            if (longUrl == null && !shortUrl.equals("favicon.ico")) { //if longUrl hasn't been found means the shortUrl was not correct
                //set headers of response packet
                out.println("HTTP/1.1 404 Not Found");
                out.println("Server: Delara");
                out.println("Date: " + new Date());
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                out.println("<div>Error: 404 Not Found</div>"); //show error message in browser
                out.flush(); // flush character output stream buffer
                //File file = new java.io.File("src/ceit/aut/ac/ir/error.html").getAbsoluteFile();
                //Desktop.getDesktop().open(file);
                //System.out.println("opened");
            } else if(!shortUrl.equals("favicon.ico")){

                //set headers for redirect response
                out.println("HTTP/1.1 302 Moved Temporarily");  //in order to not being cached by browser
                out.println("Server: Delara");
                out.println("Date: " + new Date());
                out.println("Location: " + longUrl);
                out.println("Set-Cookie: " + shortUrl + "\t" + longUrl);
                System.out.println("cookie set");
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            if (sSocket != null) {
                try {
                    sSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
