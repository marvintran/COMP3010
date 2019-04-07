//
//  Name: Marvin Tran
//  Student Number: XXXXXXX
//  Professor: Peter Graham
//  COMP 3010 - Distributed Computing - Winter 2019
//  Assignment 2 Question 3


import java.rmi.*;

public class Server
{
  public static void main (String[] argv)
  {
    try
    {
      Naming.rebind ("//owl.cs.umanitoba.ca:XXXXX/Bank", new MultiThreadBank());
      System.out.println("Server starting.");
    }
    catch (Exception e)
    {
      System.out.println ("Server failed: " + e);
    }
  }
} // class:Server
