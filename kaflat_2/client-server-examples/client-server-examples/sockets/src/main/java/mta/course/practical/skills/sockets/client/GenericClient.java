package mta.course.practical.skills.sockets.client;

import java.net.Socket;

public class GenericClient {

    public GenericClient(String host, int port) {
        try {
            //creates a socket to the server
            Socket socket = new Socket(host, port);

            System.out.println("Client Socket source IP:" + socket.getLocalAddress().getHostName());
            System.out.println("Client Socket source Port:" + socket.getLocalPort());
            System.out.println("Connected to server at IP:" + socket.getInetAddress().getHostName());
            System.out.println("Connected to server at Port:" + socket.getPort());

            //starts a thread that receives data from the server and writes it to the system console
            StreamsThread responseThread =
                    new StreamsThread(socket.getInputStream(), System.out);

            //make the thread with higher priority than this one in order to print server messages
            //even if this thread is currently handling user input
            responseThread.setPriority(Thread.currentThread().getPriority() + 1);

            //make that thread a daemon thread which means that if there are no more threads in this process
            //the process will quit
            responseThread.setDaemon(true);

            //starts the thread
            responseThread.start();

            //gets user input from the system console and sends it to the server output stream
            StreamsThread handleUserInput =
                    new StreamsThread(System.in, socket.getOutputStream());
            //no need to handle user input in a separate thread - so just call the "run" method
            //this does not creates a separate thread, just calls the logic inside the StreamThread class
            handleUserInput.run();

            //since handleUserInput is not running is a separate thread
            //we will close the socket only AFTER the user has exited the input loop
            //meaning, the client needs to shutdown
            socket.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println(
                    "Usage: java examples.streams.advanced.client.GenericClient <host> <port>");
        }
    }
    // java examples.streams.advanced.client.GenericClient <host> <port>

    public static void main(String[] args) {
//        new GenericClient(args[0], Integer.parseInt(args[1]));
        new GenericClient("localhost", 2323);
    }
}

