# COMP 3010 - Distrubuted Computing - Assignment 2

## Information

* Name: Marvin Tran
* Student Number: XXXXXXX
* Professor: Peter Graham
* Winter 2019

## Q1 - Perl and Python clients

1. complie the server in folder /Q1_Perl_Python
   javac ServMulti.java

2. Start the server - ASSUMED TO RUN ON owl.cs.umanitoba.ca
   java ServMulti

3. Run the Python client using
   python ClientPython.py

4. Run the Perl client using
   Perl ClientPerl.pl

## Q2 - Multi Threaded Stream Socket using MySQL/MariaDB

1. set up the database
  ssh into www3.cs.umanitoba.ca
  while in folder /Q2_Database, run command:
  mysql --user=YOURUSERID --password YOURDBNAME < SCRIPTFILE.sql
  this creates a table named tranm346_BANKACCOUNTS

2. compile the java source files:
	javac ServMultiDatabase.java
	javac Cli.java

3. Start the server - ASSUMED TO RUN ON www3.cs.umanitoba.ca
	java -cp :mysql-connector-java-8.0.15.jar ServMultiDatabase YOURUSERID YOURUSERPASS YOURDBNAME

4. Start the client
	java Cli

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

