# COMP 3010 - Distrubuted Computing - Assignment 1

## Information

* Name: Marvin Tran
* Student Number: XXXXXXX
* Professor: Peter Graham
* Winter 2019

## Q2 - Single Threaded

This program uses Serv.java and Cli.java

To run the program:
* Complie the two programs using 
  * javac Serv.java
  * javac Cli.java
* Run the files using 
  * java Serv
  * java Cli
* Make sure to run "java Serv" on the machine:
  * owl.cs.umanitoba.ca
* Then the java Cli can be run on any other aviary computer

Commands will be entered on the client program. There will be feedback on both the client side and server side. Only one client program can connect to the parent program at a time. If another client program tries to connect to the server, it will have to wait until the current connecting client is finished. 

## Q3 - Multi Threaded 

This program uses ServMulti.java and Cli.java

To run the program:
* Complie the two programs using 
  * javac ServMulti.java
  * javac Cli.java
* Run the files using 
  * java ServMulti
  * java Cli
* Make sure to run "java ServMulti" on the machine:
  * owl.cs.umanitoba.ca

Commands will be entered on the client program. There will be feedback on both the client side and server side. Multiple clients can connect to the server at the same time. We simply make a new thread for each Socket accepted. We also make use of synchronized methods when updating the shared Hashtable to ensure that accessing data and results of clients requests are correct.

## Q4 - Datagram

This program uses DG_Serv.java and DG_Cli.java

To run the program:
* Complie the two programs using 
  * javac DG_Serv.java
  * javac DG_Cli.java
* Run the files using 
  * java DG_Serv
  * java DG_Cli
* Make sure to run "java DG_Serv" on the machine:
  * owl.cs.umanitoba.ca

Commands will be entered on the client program. There will be feedback on both the client side and server side. Multiple clients can send messages to the server. We simply make a new thread to process each packet received. The methods are still synchronized when updating the shared Hashtable to ensure that accessing data and results of clients requests are correct.