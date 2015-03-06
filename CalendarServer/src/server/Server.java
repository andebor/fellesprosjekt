package server;
////
import java.net.*;
import java.io.*;
 
public class Server {
    public static void main(String[] args) throws IOException {
    	
        int port = 6066;
        
        boolean listening = true;
        
        try (ServerSocket serverS = new ServerSocket(port)) { 
        	System.out.println("Server started on port " + port);
            while (listening) {
                new ServerThread(serverS.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
    }
}