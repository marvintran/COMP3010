//
// Skeleton code for implementing a multi-threaded server using Java stream
// sockets. Your class will need a 'main' method in which the core server loop
// is coded and a 'run' method which will be executed for each thread
// created. Since threads are created for each client connection, the run
// method will contain the actual service code. Thus, after accepting a
// connection and getting a client socket, the main code will need to create
// and start a Thread for that client and pass it the client socket so it
// know who to communicate with while providing the service. To support
// Threads, your class must implement the 'Runnable' interface. 
//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019

import java.net.*;
import java.io.*;
import java.util.Hashtable;

public class DG_Serv implements Runnable
{
  Bank threadBankReference;

  DatagramSocket serverSocket;
  DatagramPacket thePacket;

  // constructor called by server for each client
  DG_Serv(DatagramSocket serverSocket, DatagramPacket thePacket, Bank threadBankReference)
  {
    this.serverSocket = serverSocket;
    this.thePacket = thePacket;
    this.threadBankReference = threadBankReference;
  }

  //The method run when the server is started from the command line
  public static void main(String[] args)
  {
    // Datagram stuff
    DatagramSocket sock = null;  	// server's master socket
    DatagramPacket packet = null;	// the datagram packet that we receive from sock

    // buffer for datagram packet we get from sock
    byte[] receiveData = new byte[1024];

    Bank theBank = new Bank();// where the hashtable is stored

    System.out.println("Server starting.");

	  // Create socket
    try
    {
      // create socket to receive packets on
      sock = new DatagramSocket(XXXXX);
    }
    catch (Exception e)
    {
      System.out.println("Creation of DataGram Socket failed.");
      System.exit(1);
    }
  
    // Loop forever accepting packages and create a thread for each packet
    while (true)
    {
      // Receive a datagram packet
      try
      {
        packet = new DatagramPacket(receiveData, receiveData.length);
        sock.receive(packet);
      }
      catch (Exception e)
      {
        System.err.println("LookupServer: socket receive failed.");
        System.exit(1);
      }

    // Create a thread and pass this packet to do the command contained in the packet
    // pass the socket where packets will be sent
    // Pass theBank so each thread can update the same hashTable
    // then start the run() method
	  new Thread(new DG_Serv(sock, packet, theBank)).start();
	
	  } // end while - accept
	
    // Will never get here - its a server!

  } // end main

  // The method run by each thread when it is started
  public void run()
  {
    // datagram stuff
    DatagramPacket sendPacket;// the packet we send back to the server that sent thePacket

    InetAddress clientIP = null; // address of server that thePacket came from
    int clientPort = 0; // port of the server that thePacket came from

    // buffer for datagram packet that we will send back to the sending server
    byte[] sendData = new byte[1024];

    // Server Created Stuff
    String serverSentence = ""; // datagram packet to string

    // From Client
    String clientSentence = ""; // datagram packet to string

    // other variables
    String[] tokens; // splits the command in clientSentence that we got from thePacket
    int accountNumber; // it's stored in tokens[1] for every command

    // get the address and port where this packet was sent,
    // then send welcome message to client after receiving packet
    try
    {
      clientIP = thePacket.getAddress();
      clientPort = thePacket.getPort();

      serverSentence = "Welcome to the bank! Please use one of the following commands. " +
        "C<account>, R<account>, D<account,amount>, W<account,amount>";

      // convert the sentence to bytes and put it in the sendData byte array
      sendData = serverSentence.getBytes();

      // make a new packet with the data in sendData and specify the clientIP addr and clientPort to sent it to
      sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
      serverSocket.send(sendPacket);
    }
    catch (Exception e)
    {
      System.out.println("Terminal read or socket output failed.");
      System.exit(1);
    }

    // now actually process the packet this thread was given and do work on it

    // getting string from datagram packet
    clientSentence = new String(thePacket.getData(), 0, thePacket.getLength());

    // A Create or Retrieve command has the format C<account> or R<account>
    // where account is a number from 0-9
    if( clientSentence.matches("[CR]<[0-9]+>"))
    {
      tokens = clientSentence.split("<|>");

      // Create Account - C<account>
      if(tokens[0].equals("C"))
      {
        System.out.println("Client sent Create Command");
        accountNumber = Integer.parseInt(tokens[1]);

        serverSentence = threadBankReference.createAccount(accountNumber);

      }// Create Account - C<account>

      // Retrieve Account - R<account>
      else if(tokens[0].equals("R"))
      {
        System.out.println("Client sent Retrieve Command");
        accountNumber = Integer.parseInt(tokens[1]);

        serverSentence = threadBankReference.retrieveAccount(accountNumber);

      }// Retrieve Account - R<account>

    }// if command: C<account> or R<account>

    // A Deposit or Withdraw command has the format D<account,amount> or W<account,amount>
    // where account and amount are numbers from 0-9
    else if( clientSentence.matches("[DW]<[0-9]+,[0-9]+>"))
    {
      tokens = clientSentence.split("<|>|,");

      // Deposit into account - D<account,amount>
      if(tokens[0].equals("D"))
      {
        System.out.println("Client sent Deposit Command");
        accountNumber = Integer.parseInt(tokens[1]);
        int depositAmount = Integer.parseInt(tokens[2]);

        serverSentence = threadBankReference.depositAccount(accountNumber, depositAmount);
      }// Deposit into account - D<account,amount>

      // Withdraw from account - W<account,amount>
      else if(tokens[0].equals("W"))
      {
        System.out.println("Client sent Withdraw Command");
        accountNumber = Integer.parseInt(tokens[1]);
        int withdrawAmount = Integer.parseInt(tokens[2]);

        serverSentence = threadBankReference.withdrawAccount(accountNumber, withdrawAmount);

      }// Withdraw from account - W<account,amount>

    }// if command: D<account,amount> or W<account,amount>

    // Client will Exit
    else if(clientSentence.equals("E"))
    {
      serverSentence = "Exit Command Received. Goodbye.";
      System.out.println("Client sent Exit Command\n");
    }

    // Invalid Command,
    // it's not in the format: capital letter, < bracket, number, > bracket
    // or in the format: capital letter, < bracket, number, comma, number, > bracket
    else
    {
      serverSentence = "Invalid command or format for " + clientSentence + ". Available commands: " +
        "C<account>, R<account>, D<account,amount>, W<account,amount>";
      System.out.println("Client sent invalid "+ clientSentence + " command\n");
    }// invalid command

    // send a message back to client
    try
    {
      // convert the sentence to bytes and put it in the sendData byte array
      sendData = serverSentence.getBytes();

      // make a new packet with the data in sendData and specify the clientIP addr and clientPort to sent it to
      sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
      serverSocket.send(sendPacket);
    }
    catch (Exception e)
    {
      System.out.println("Terminal read or socket output failed.");
      System.exit(1);
    }
  } // end run

} // end class:BankServ


// this class holds the Hashtable and we pass an object of this class to every thread (the same object to each class)
// this way, each thread has access to the same Hashtable to make changes to it
// but instead of getting a reference to the Hashtable in the run() and updating the Hashtable in the run()
// we move code that interacts with the hash table inside of the object and turn them into methods
// So the run() calls these methods to interact with the hashtable
// then we make those methods 'synchronized', so we eliminate the concurency problem
// this eliminates the concurrency problem by making it so that only one thread can use a method at a time

class Bank
{
  Hashtable<Integer, Integer> bank;

  // constructor
  public Bank()
  {
    bank = new Hashtable<Integer, Integer>();
  }

  public synchronized String createAccount(int accountNumber)
  {
    String serverSentence;

    // if the bank does not contain this account number,
    // make a new account with balance zero
    if(!bank.containsKey(accountNumber))
    {
      bank.put(accountNumber, 0);
      serverSentence = "Created account \"" + accountNumber + "\" successfully";
      System.out.println("Added account \"" + accountNumber + "\" to the bank successfully\n");
    }

    // This account number is already in the bank
    else
    {
      serverSentence = "Account with number \"" + accountNumber + "\" already exists";
      System.out.println("Account number \"" + accountNumber + "\" is already in the bank\n");
    }

    return serverSentence;
  }// createAccount

  public synchronized String retrieveAccount(int accountNumber)
  {
    String serverSentence;

    // if we find this account number, send back the accountBalance
    if(bank.containsKey(accountNumber))
    {
      int accountBalance = bank.get(accountNumber);
      serverSentence = "Account \"" + accountNumber + "\" has balance: $" + accountBalance;
      System.out.println("Retrieved account \"" + accountNumber + "\" successfully\n");
    }

    // This account number doesn't exist in the bank
    else
    {
      serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank";
      System.out.println("Account \"" + accountNumber + "\" does not exist in the bank\n");
    }

    return serverSentence;
  }// retrieveAccount

  public synchronized String depositAccount(int accountNumber, int depositAmount)
  {
    String serverSentence;

    // check to see if this account number even exists
    if(bank.containsKey(accountNumber))
    {
      // If an existing key is passed then the previous value gets replaced by the new value
      // so replace it with the current balance plus the amountDeposit
      bank.put(accountNumber, bank.get(accountNumber) + depositAmount);
      serverSentence = "Deposited $" + depositAmount + " successfully. " +
        "New balance: $" + bank.get(accountNumber);
      System.out.println("Deposited $"+ depositAmount +
        " to account \"" + accountNumber +
        "\" successfully\n");
    }

    // This account number doesn't exist in the bank
    else
    {
      serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank to deposit into";
      System.out.println("Account \"" + accountNumber + "\" does not exist in the bank to deposit into\n");
    }

    return serverSentence;
  }// depositAccount

  public synchronized String withdrawAccount(int accountNumber, int withdrawAmount)
  {
    String serverSentence;

    // check to see if this account number even exists
    if(bank.containsKey(accountNumber))
    {
      // check to see if we can even withdraw this amount from the account
      if( bank.get(accountNumber) - withdrawAmount >= 0 )
      {
        // If an existing key is passed then the previous value gets replaced by the new value
        // so replace it with the current balance minus the withdrawAmount
        bank.put(accountNumber, bank.get(accountNumber) - withdrawAmount);
        serverSentence = "Withdrew $" + withdrawAmount + " successfully. " +
          "New balance: $" + bank.get(accountNumber);
        System.out.println("Withdrew $" + withdrawAmount +
          " from account \"" + accountNumber +
          "\" successfully\n");
      }

      // this account does not have enough funds to withdraw from
      else
      {
        serverSentence = "There are not enough funds to withdraw $" + withdrawAmount +
          ". Balance is: $" + bank.get(accountNumber);
        System.out.println("Account \"" + accountNumber +
          "\" has $" + bank.get(accountNumber) +
          " and cannot withdraw $" + withdrawAmount + "\n");
      }
    }

    // This account number doesn't exist in the bank
    else
    {
      serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank to withdraw from";
      System.out.println("Account \"" + accountNumber + "\" does not exist in the bank to withdraw from\n");
    }

    return serverSentence;
  }// withdrawAccount

}// end class Bank