package ceit.aut.ac.ir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class writeToFile {

    File file;
    BufferedWriter writer;

    public writeToFile(String address) throws IOException {
        file = new File(address);

    }

    public void writeToFile(String shortUrl, String longUrl) {
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(shortUrl + "\t" + longUrl + "\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
