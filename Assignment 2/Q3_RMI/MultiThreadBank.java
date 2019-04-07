//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019
//  Assignment 2 Question 3

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class MultiThreadBank extends UnicastRemoteObject
  implements MultiThreadBankIface {

  private Bank theBank;

  public MultiThreadBank() throws RemoteException
  {
    theBank = new Bank();
  }

  public String connected() throws RemoteException
  {
    System.out.println("A Client has connected\n");
    String welcomeMessage = "Welcome to the bank! Please use one of the following commands.\n" +
                            "C<account>, R<account>, D<account,amount>, W<account,amount>\n" +
                            "To exit type: E, to print out all accounts and their balances: B";
    return welcomeMessage;
  }

  public String createAccount(int accountNumber) throws RemoteException
  {
    System.out.println("Client sent Create Command");
    return theBank.createAccount(accountNumber);
  }

  public String retrieveAccount(int accountNumber) throws RemoteException
  {
    System.out.println("Client sent Retrieve Command");
    return theBank.retrieveAccount(accountNumber);
  }

  public String depositAccount(int accountNumber, int depositAmount) throws RemoteException
  {
    System.out.println("Client sent Deposit Command");
    return theBank.depositAccount(accountNumber, depositAmount);
  }

  public String withdrawAccount(int accountNumber, int withdrawAmount) throws RemoteException
  {
    System.out.println("Client sent Withdraw Command");
    return theBank.withdrawAccount(accountNumber, withdrawAmount);
  }

  public String exiting() throws RemoteException
  {
    System.out.println("Client sent Exit Command\n");
    return "Exit Command Received. Goodbye.";
  }

  public String accountSummary() throws RemoteException
  {
    System.out.println("Printing out all accounts in the bank\n");
    return theBank.accountSummary();
  }
}


class Bank
{
  private Hashtable<Integer, Integer> bank;

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

  public synchronized String accountSummary()
  {
    String serverSentence = "";

    // getting key and value pairs from hashtable
    //http://www.java2s.com/Tutorial/Java/0140__Collections/GettingelementskeyvaluepairsfromaHashtabletheentrySetmethod.htm
    Set set = bank.entrySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      serverSentence += "Account: " + entry.getKey() +
                       " - Balance: " + entry.getValue() + "\n";
    }

    if(serverSentence.equals(""))
      serverSentence = "There are no accounts in the bank\n";

    return serverSentence;
  }
}