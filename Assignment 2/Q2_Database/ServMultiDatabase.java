//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019
//  Assignment 2 Question 2

import java.net.*;
import java.io.*;
import java.sql.*;

public class ServMultiDatabase implements Runnable
{
  Bank threadBankReference;

  // *** This is instantiated per client whenever
  // a new Thread is created
  Socket threadClientSocket = null;  // socket for each client

  // constructor called by server for each client
  ServMultiDatabase(Socket threadClientSocket, Bank threadBankReference)
  {
    this.threadClientSocket = threadClientSocket;
    this.threadBankReference = threadBankReference;
  }

  // The method run when the server is started from the command line
  public static void main(String[] args)
  {
    System.out.println("\nServer starting.\n");

    ServerSocket sock = null;    // server's master socket
    InetAddress addr = null;     // address of server
    Socket clientSocket = null;  // client socket returned from accept

    Bank theBank = null;

    try
    {
      //for(int i = 0; i < args.length; i++)
      //  System.out.println(args[i]);
      theBank = new Bank(args[0], args[1], args[2]);
    }
    catch (Exception e)
    {
      System.err.println("Enter YOURUSERID YOURUSERPASS YOURDBNAME " +
                         "as command line arguments");
      System.err.println("java -cp :mysql-connector-java-8.0.15.jar " +
                          "ServMultiDatabase " +
                          "YOURUSERID YOURUSERPASS YOURDBNAME");
      System.exit(1);
    }

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
      new Thread(new ServMultiDatabase(clientSocket, theBank)).start();

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
  private Connection con;

  public Bank(String uid, String pw, String db)
  {
    System.out.println("Connecting to Database.");

    // First get a JDBC driver, in this case for a mySQL/MariaDB database
    try {
      // This is deprecated, use the new one
      // Class.forName("com.mysql.jdbc.Driver");
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (Exception e) {
      System.out.println("Couldn't get the needed JDBC driver");
      e.printStackTrace();
      System.exit(44);
    } // end try-catch


    // Next, get a connection to the database
    con = null;

    try
    {
      // String srvr = "silicon.cs.umanitoba.ca";
      String srvr = "127.0.0.1";
      String port = "3306";

      // construct URL address for database
      String url = "jdbc:mysql://"+srvr+":"+port+"/"+db;

      // make connection to database
      con = DriverManager.getConnection(url, uid, pw );
    }
    catch( Exception e )
    {
      System.out.println("Connection failed");
      e.printStackTrace();
      System.exit(55);
    } // end try-catch

    System.out.println("Successfully connected to Database.\n");
  }

  public synchronized String createAccount(int accountNumber)
  {
    Statement st = null;
    String serverSentence = "";

    try
    {
      // setup an SQL statement on the connection to use to make a query
      st = con.createStatement();

      // try to get the row with this accountNumber
      ResultSet rs = st.executeQuery("select * from tranm346_BANKACCOUNTS " +
                                     "where accNum = " + accountNumber + ";");

      // if the returned set contains a row,
      // this accountNumber is already in the database
      if(rs.next())
      {
        serverSentence = "Account with number \"" + accountNumber + "\" already exists";
        System.out.println("Account number \"" + accountNumber + "\" is already in the bank\n");
      }

      // if the returned set does not contain a row,
      // add this accountNumber to the database
      else
      {
        // inserting into database
        // https://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
        String sql = "INSERT INTO tranm346_BANKACCOUNTS " +
                     "VALUES (" + accountNumber + ", 0)";

        st.executeUpdate(sql);
        serverSentence = "Created account \"" + accountNumber + "\" successfully";
        System.out.println("Added account \"" + accountNumber + "\" to the bank successfully\n");
      }
    }
    catch( Exception e )
    {
      System.out.println("Problem accessing the database/results.");
      e.printStackTrace();
      System.exit(66);
    } // end try-catch

    return serverSentence;
  }// createAccount

  public synchronized String retrieveAccount(int accountNumber)
  {
    Statement st = null;
    String serverSentence = "";

    try
    {
      // setup an SQL statement on the connection to use to make a query
      st = con.createStatement();

      // try to get the row with this accountNumber
      ResultSet rs = st.executeQuery("select * from tranm346_BANKACCOUNTS " +
        "where accNum = " + accountNumber + ";");

      // if the returned set contains a row,
      // this accountNumber exists in the database,
      // get the balance and add it to serverSentence to send back to the client
      if(rs.next())
      {
        int balance = rs.getInt(2);

        serverSentence = "Account \"" + accountNumber + "\" has balance: $" + balance;
        System.out.println("Retrieved account \"" + accountNumber + "\" successfully\n");
      }

      // if the returned set does not contain a row,
      // this account number doesn't exist in the database
      else
      {
        serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank";
        System.out.println("Account \"" + accountNumber + "\" does not exist in the bank\n");
      }
    }
    catch( Exception e )
    {
      System.out.println("Problem accessing the database/results.");
      e.printStackTrace();
      System.exit(66);
    } // end try-catch

    return serverSentence;
  }// retrieveAccount

  public synchronized String depositAccount(int accountNumber, int depositAmount)
  {
    Statement st = null;
    String serverSentence = "";

    try
    {
      // setup an SQL statement on the connection to use to make a query
      st = con.createStatement();

      // try to get the row with this accountNumber
      ResultSet rs = st.executeQuery("select * from tranm346_BANKACCOUNTS " +
        "where accNum = " + accountNumber + ";");

      // if the returned set contains a row,
      // this accountNumber exists in the database,
      // update the account with the new balance after depositing
      if(rs.next())
      {
        int balance = rs.getInt(2);
        int newBalance = balance + depositAmount;
        String sql = "UPDATE tranm346_BANKACCOUNTS " +
                     "SET balance = " + newBalance +
                     " WHERE accNum = "+ accountNumber + ";";

        st.executeUpdate(sql);

        serverSentence = "Deposited $" + depositAmount + " successfully. " +
          "New balance: $" + newBalance;
        System.out.println("Deposited $"+ depositAmount +
          " to account \"" + accountNumber +
          "\" successfully\n");}

      // if the returned set does not contain a row,
      // this account number doesn't exist in the database
      else
      {
        serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank to deposit into";
        System.out.println("Account \"" + accountNumber + "\" does not exist in the bank to deposit into\n");
      }
    }
    catch( Exception e )
    {
      System.out.println("Problem accessing the database/results.");
      e.printStackTrace();
      System.exit(66);
    } // end try-catch

    return serverSentence;
  }// depositAccount

  public synchronized String withdrawAccount(int accountNumber, int withdrawAmount)
  {
    Statement st = null;
    String serverSentence = "";

    try
    {
      // setup an SQL statement on the connection to use to make a query
      st = con.createStatement();

      // try to get the row with this accountNumber
      ResultSet rs = st.executeQuery("select * from tranm346_BANKACCOUNTS " +
        "where accNum = " + accountNumber + ";");

      // if the returned set contains a row,
      // this accountNumber exists in the database,
      // update the account with the new balance after withdrawing
      if(rs.next())
      {
        // check to see if we can even withdraw this amount from the account
        int balance = rs.getInt(2);

        if (balance - withdrawAmount >= 0)
        {
          int newBalance = balance - withdrawAmount;
          String sql = "UPDATE tranm346_BANKACCOUNTS " +
            "SET balance = " + newBalance +
            " WHERE accNum = " + accountNumber + ";";

          st.executeUpdate(sql);

          serverSentence = "Withdrew $" + withdrawAmount + " successfully. " +
            "New balance: $" + newBalance;
          System.out.println("Withdrew $" + withdrawAmount +
            " from account \"" + accountNumber +
            "\" successfully\n");
        }

        // this account does not have enough funds to withdraw from
        else
        {
          serverSentence = "There are not enough funds to withdraw $" + withdrawAmount +
            ". Balance is: $" + balance;
          System.out.println("Account \"" + accountNumber +
            "\" has $" + balance +
            " and cannot withdraw $" + withdrawAmount + "\n");
        }

      }

      // if the returned set does not contain a row,
      // this account number doesn't exist in the database
      else
      {
        serverSentence = "There is no account with number \"" + accountNumber + "\" in the bank to withdraw from";
        System.out.println("Account \"" + accountNumber + "\" does not exist in the bank to withdraw from\n");
      }
    }
    catch( Exception e )
    {
      System.out.println("Problem accessing the database/results.");
      e.printStackTrace();
      System.exit(66);
    } // end try-catch

    return serverSentence;
  }// withdrawAccount

}