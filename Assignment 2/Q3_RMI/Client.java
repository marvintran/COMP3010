//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019
//  Assignment 2 Question 3

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;

public class Client
{
  public static void main (String[] argv)
  {
    // Client Created Stuff
    BufferedReader inFromUser = null;
    String clientSentence = "";
    MultiThreadBankIface theBank = null;

    // From Server
    String serverSentence = "";

    // other variables
    String[] tokens; // splits the command in clientSentence
    int accountNumber; // it's stored in tokens[1] for every command

    System.out.println("Client starting.");

    // instantiate an instance of the remote interface and get welcome message
    try
    {
      theBank = (MultiThreadBankIface) Naming.lookup ("//owl.cs.umanitoba.ca:XXXXX/Bank");
      String welcomeMessage = theBank.connected();
      System.out.println(welcomeMessage + "\n");
    }
    catch (Exception e)
    {
      System.out.println ("Client exception:"+ e);
    }

    // set up BufferedReader for the user to input commands
    inFromUser = new BufferedReader(new InputStreamReader(System.in));

    // read and send commands to server by invoking methods on the interface object
    do
    {
      try
      {
        clientSentence = inFromUser.readLine();// wait for user input here
      }
      catch (Exception e)
      {
        System.out.println("Failed to get input from user.");
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
          accountNumber = Integer.parseInt(tokens[1]);

          try
          {
            serverSentence = theBank.createAccount(accountNumber);
            System.out.println(serverSentence + "\n");
          }
          catch (Exception e)
          {
            System.out.println("Could not create account.");
            System.exit(1);
          }
        }// Create Account - C<account>

        // Retrieve Account - R<account>
        else if(tokens[0].equals("R"))
        {
          accountNumber = Integer.parseInt(tokens[1]);

          try
          {
            serverSentence = theBank.retrieveAccount(accountNumber);
            System.out.println(serverSentence + "\n");
          }
          catch (Exception e)
          {
            System.out.println("Could not retrieve account.");
            System.exit(1);
          }
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
          accountNumber = Integer.parseInt(tokens[1]);
          int depositAmount = Integer.parseInt(tokens[2]);

          try
          {
            serverSentence = theBank.depositAccount(accountNumber, depositAmount);
            System.out.println(serverSentence + "\n");
          }
          catch (Exception e)
          {
            System.out.println("Could not deposit into account.");
            System.exit(1);
          }
        }// Deposit into account - D<account,amount>

        // Withdraw from account - W<account,amount>
        else if(tokens[0].equals("W"))
        {

          accountNumber = Integer.parseInt(tokens[1]);
          int withdrawAmount = Integer.parseInt(tokens[2]);

          try
          {
            serverSentence = theBank.withdrawAccount(accountNumber, withdrawAmount);
            System.out.println(serverSentence + "\n");
          }
          catch (Exception e)
          {
            System.out.println("Could not deposit into account.");
            System.exit(1);
          }
        }// Withdraw from account - W<account,amount>

      }// if command: D<account,amount> or W<account,amount>

      // Client will Exit
      else if(clientSentence.equals("E"))
      {
        try
        {
          serverSentence = theBank.exiting();
          System.out.println(serverSentence + "\n");
        }
        catch (Exception e)
        {
          System.out.println("Problems getting exit message.");
          System.exit(1);
        }
      }

      // For each account, print out the account number and their current balance
      else if(clientSentence.equals("B"))
      {
        System.out.println("Printing out all of the accounts and their balances");
        try
        {
          serverSentence = theBank.accountSummary();
          System.out.println(serverSentence);
        }
        catch (Exception e)
        {
          System.out.println("Problems getting all of the accounts.");
          System.exit(1);
        }
      }

      // Invalid Command,
      // it's not in the format: capital letter, < bracket, number, > bracket
      // or in the format: capital letter, < bracket, number, comma, number, > bracket
      else
      {
        System.out.println("Invalid command or format for " + clientSentence + "\nAvailable commands:" +
                           "C<account>, R<account>, D<account,amount>, W<account,amount>, E, B\n");
      }// invalid command

    } while (!clientSentence.equals("E")) ;

    // close the stream
    try
    {
      inFromUser.close();
    }
    catch (Exception e)
    {
      System.out.println("Client couldn't close stream.");
      System.exit(1);
    }

    System.out.println("Client finished.");

  }
} // class:Client
