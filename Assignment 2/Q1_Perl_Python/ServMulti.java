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

public class ServMulti implements Runnable
{
  Bank threadBankReference;

  Socket threadClientSocket = null;  // socket for each client
                          // *** This is instantiated per client whenever
	                      // a new Thread is created

  // constructor called by server for each client
  ServMulti(Socket threadClientSocket, Bank threadBankReference)
  {
    this.threadClientSocket = threadClientSocket;
    this.threadBankReference = threadBankReference;
  }

  // The method run when the server is started from the command line
  public static void main(String[] args)
  {
    ServerSocket sock = null;    // server's master socket
    InetAddress addr = null;     // address of server
    Socket clientSocket = null;           // client socket returned from accept

    Bank theBank = new Bank();

    System.out.println("Server starting.");

	  // Create main ServerSocket
    try
    {
      addr = InetAddress.getLocalHost();

      // create server socket on port XXXXX with backlog of 3 on this machine
      sock = new ServerSocket(XXXXX,3,addr); // create server socket:
    }
    catch (Exception e)
    {
      System.err.println("Creation of ServerSocket failed.");
      System.exit(1);
    }
  
    // Loop forever accepting client connections
    while (true)
    {
      // Accept a connection
      try
      {
        clientSocket = sock.accept(); // accept a connection from client
        System.out.println("A Client has connected\n");
      }
      catch (Exception e)
      {
        System.err.println("Accept failed.");
        System.exit(1);
      }
	
    // Create a thread for this client (which starts our run() method
	  new Thread(new ServMulti(clientSocket, theBank)).start();
	
	} // end while - accept
	
    // Will never get here - its a server!

  } // end main

  // The method run by each thread when it is started
  public void run()
  {
    // Server Created Stuff
    String serverSentence = "";

    // From Client
    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null; // stream used to read from socket
    String clientSentence = "";

    // other variables
    String[] tokens; // splits the command in clientSentence
    int accountNumber; // it's stored in tokens[1] for every command

    // send welcome message to client after successful accept connection
    try
    {
      outToClient = new DataOutputStream(threadClientSocket.getOutputStream());
      serverSentence = "Welcome to the bank! Please use one of the following commands. " +
        "C<account>, R<account>, D<account,amount>, W<account,amount>";
      outToClient.writeBytes(serverSentence + '\n');
    }
    catch (Exception e)
    {
      System.out.println("Could not send message back to client after successful connect.");
      System.exit(1);
    }

    // getting data from clientSocket
    try
    {
      inFromClient = new BufferedReader(new InputStreamReader(threadClientSocket.getInputStream()));
    }
    catch (Exception e)
    {
      System.out.println("Couldn't create socket input stream.");
      System.exit(1);
    }

    // Read commands from the connection and print them until an "E" is received
    do
    {
      try
      {
        clientSentence = inFromClient.readLine();
      }
      catch (Exception e)
      {
        System.out.println("Could not read message from Socket");
        System.exit(1);
      }

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
        outToClient.writeBytes(serverSentence + '\n');
      }
      catch (Exception e)
      {
        System.out.println("Could not send message.");
        System.exit(1);
      }
    } while (!clientSentence.equals("E"));

    // close the socket
    try
    {
      threadClientSocket.close();
    }
    catch (Exception e)
    {
      System.err.println("couldn't close client socket.");
      System.exit(1);
    }
  } // end run

} // end class:BankServ

class Bank
{
  Hashtable<Integer, Integer> bank;

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

}