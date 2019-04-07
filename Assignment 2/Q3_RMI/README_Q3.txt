# COMP 3010 - Distrubuted Computing - Assignment 2

## Information

* Name: Marvin Tran
* Student Number: XXXXXXX
* Professor: Peter Graham
* Winter 2019

## Q3 - Multi Threaded RMI

1. compile the java source files in folder /Q3_RMI:
	javac MultiThreadBank.java
	javac MultiThreadBankIface.java
	javac Client.java
	javac Server.java
  Alternatively do: javac *.java

2. Start the registry on port XXXXX
	rmiregistry XXXXX &

3. Start the server - ASSUMED TO RUN ON OWL.CS.UMANITOBA.CA
	java Server &

4. Start the client
	java Client