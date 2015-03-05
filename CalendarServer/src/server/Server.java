package server;

import java.net.*;
import java.io.*;
 
public class Server extends Thread {
	
	private ServerSocket serverSocket;
	
	public Server(int port) throws IOException {
	      serverSocket = new ServerSocket(port);
	      serverSocket.setSoTimeout(10000);
	}
	
   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("Message to client from server: Thx forconnecting to "
              + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out cuz of 10 sec inaactivity!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
	
   public static void main(String [] args)
   {
	  int port = 6066;
      try
      {
         Thread t = new Server(port);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}