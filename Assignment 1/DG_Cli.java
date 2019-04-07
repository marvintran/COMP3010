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

public class DG_Cli
{
  public static void main(String[] args)
  {
    // Datagram stuff
    DatagramSocket sock = null;        // client's DATAGRAM socket
    DatagramPacket sendPacket = null;
    DatagramPacket receivePacket = null;
    InetAddress srvAddr = null;         // addr of server (local host for now)

    // buffer for datagram packet payload
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    // Client Created Stuff
    BufferedReader inFromUser = null;
    String clientSentence = ""; // datagram packet to string

    // From Server
    String serverSentence = ""; // datagram packet to string

    System.out.println("Client starting.");

    // create socket to receive packets from and create address to sent packets to
    try
    {
      srvAddr = InetAddress.getByName("owl.cs.umanitoba.ca");
      sock = new DatagramSocket(XXXXX); // create client socket
    }
    catch (Exception e)
    {
      System.err.println("Creation of Client DataGramSocket failed.");
      System.exit(1);
    } // end try-catch

    // read and send commands through to server using DatagramPackets until "E" is entered
    do
    {
      inFromUser = new BufferedReader(new InputStreamReader(System.in));

      // send packet/command to server
      try
      {
        clientSentence = inFromUser.readLine();

        // convert the sentence to bytes and put it in the sendData byte array
        sendData = clientSentence.getBytes();

        // make a new packet with the data in sendData and specify the srvAddr and port to sent it to
        sendPacket = new DatagramPacket(sendData, sendData.length, srvAddr, XXXXX);
        sock.send(sendPacket);
      }
      catch (Exception e)
      {
        System.out.println("Terminal read or socket output failed.");
        System.exit(1);
      }

      // receive packet/message from server
      try
      {
        // receive welcome message with commands
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        sock.receive(receivePacket);
        serverSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(serverSentence);

        // receive the feedback results of our command
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        sock.receive(receivePacket);
        serverSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(serverSentence + "\n");
      }
      catch (Exception e)
      {
        System.out.println("Error getting from server");
        System.exit(1);
      }

    } while (!clientSentence.equals("E")) ;

    // close the streams
    try
    {
      inFromUser.close();
    }
    catch (Exception e)
    {
      System.out.println("Client couldn't close socket.");
      System.exit(1);
    }

    System.out.println("Client finished.");

  } // main
}
