//
// Cli.java
//	- this is the client code which connects to the server on the machine
//	  owl.cs.umanitoba.ca and sends integers entered on the keyboard to
//	  the server until a zero is entered.
//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019

import java.net.*;
import java.io.*;

public class Cli
{
  public static void main(String[] args)
  {
    // socket variables
    Socket clientSocket = null;      // client's socket
    InetAddress addr = null;         // addr of server (local host for now)

    // Client Created Stuff
    DataOutputStream outToServer = null; // stream used to write to socket
    BufferedReader inFromUser = null;
    String clientSentence = "";

    // From Server
    BufferedReader inFromServer = null;
    String serverSentence = "";

    System.out.println("Client starting.");

    // create socket
    try
    {
      addr = InetAddress.getByName("owl.cs.umanitoba.ca");
      clientSocket = new Socket(addr,XXXXX); // create client socket
    }
    catch (Exception e)
    {
      System.out.println("Creation of client's Socket failed.");
      System.exit(1);
    }

    // set up user terminal input and socket output streams
    try
    {
      inFromUser = new BufferedReader(new InputStreamReader(System.in));
      outToServer = new DataOutputStream(clientSocket.getOutputStream());
    }
    catch (Exception e)
    {
      System.out.println("Socket output stream failed.");
      System.exit(1);
    }

    // after successful connection, read welcome message from server
    try
    {
      inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      serverSentence = inFromServer.readLine();
      System.out.println(serverSentence + "\n");
    }
    catch (Exception e)
    {
      System.out.println("Could not read welcome message from server.");
      System.exit(1);
    }

    // read and send commands over the socket until "E" is entered
    do
    {
      // send command to server
      try
      {
        clientSentence = inFromUser.readLine();
        outToServer.writeBytes(clientSentence + '\n');
      }
      catch (Exception e)
      {
        System.out.println("Terminal read or socket output failed.");
        System.exit(1);
      }

      // receive message from server
      try
      {
        serverSentence = inFromServer.readLine();
        System.out.println(serverSentence + "\n");
      }
      catch (Exception e)
      {
        System.out.println("Error getting from server");
        System.exit(1);
      }

    } while (!clientSentence.equals("E")) ;

    // close the streams and socket
    try
    {
      inFromServer.close();
      inFromUser.close();
      outToServer.close();
      clientSocket.close();
    }
    catch (Exception e)
    {
      System.out.println("Client couldn't close socket.");
      System.exit(1);
    }

    System.out.println("Client finished.");

  } // main
} // class:Cli
