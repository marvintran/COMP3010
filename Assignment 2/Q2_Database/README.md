# COMP 3010 - Distrubuted Computing - Assignment 2

## Information

* Name: Marvin Tran
* Student Number: XXXXXXX
* Professor: Peter Graham
* Winter 2019

## Q2 - Multi Threaded Stream Socket using MySQL/MariaDB

1. set up the database
  ssh into www3.cs.umanitoba.ca
  while in folder /Q2_Database, run command:
   * mysql --user=YOURUSERID --password YOURDBNAME < SCRIPTFILE.sql
  this creates a table named tranm346_BANKACCOUNTS

2. compile the java source files:
	* javac ServMultiDatabase.java
	* javac Cli.java

3. Start the server - ASSUMED TO RUN ON www3.cs.umanitoba.ca
	* java -cp :mysql-connector-java-8.0.15.jar ServMultiDatabase YOURUSERID YOURUSERPASS YOURDBNAME

4. Start the client
	* java Cli