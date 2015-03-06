package server;

import java.net.*;
import java.io.*;
 
//


public class ServerThread extends Thread {
    private Socket socket = null;
 
    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
    }
     
    public void run() {
 
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
        	String inputLine, outputLine;
            
            ServerProtocol protocol = new ServerProtocol();
            
            while ((inputLine = in.readLine()) != null) {
            	try {
            		outputLine = protocol.processInput(inputLine) + "\r\n";
            	} catch(Exception ArrayIndexOutOfBoudsException){
        			outputLine = "Invalid input \r\n";
        		}
                System.out.println(outputLine);
                out.println(outputLine);
                
                if (outputLine.equals("Bye.\r\n"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (Exception e) {
			//e.printStackTrace();
		}
        
    }
}