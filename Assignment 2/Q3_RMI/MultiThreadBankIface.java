//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019
//  Assignment 2 Question 3

import java.rmi.*;

// Remote Interface for multi threaded bank.

public interface MultiThreadBankIface extends Remote
{
  public String connected() throws RemoteException;

  public String createAccount(int accountNumber) throws RemoteException;

  public String retrieveAccount(int accountNumber) throws RemoteException;

  public String depositAccount(int accountNumber, int depositAmount) throws RemoteException;

  public String withdrawAccount(int accountNumber, int withdrawAmount) throws RemoteException;

  public String exiting() throws RemoteException;

  public String accountSummary() throws RemoteException;
}
