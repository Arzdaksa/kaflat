package mta.course.practical.skills.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class StreamsThread extends Thread {
    InputStream in;
    OutputStream out;

    //listens to input from the input stream and for every line that arrives
    //sends it to the output stream
    //can be run in a thread or just as a method
    StreamsThread(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out)) ){
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                writer.flush();
            }
        } catch (IOException x) {
        }
    }
}

