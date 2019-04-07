#!/usr/bin/perl
#  Name: Marvin Tran
#  Student Number: XXXXXXX
#  Professor: Peter Graham
#  COMP 3010 - Distributed Computing - Winter 2019
#  Assignment 2 Question 1

use strict;
use warnings FATAL => 'all';
use Socket;     # This defines PF_INET and SOCK_STREAM

sub StartClient {

    print "Client starting.\n";

    # Create a socket object and connect to it
    my $client_socket = socket(SOCKET,PF_INET,SOCK_STREAM,(getprotobyname('tcp'))[2])
        or die "Can't create a socket $!\n";
    my $host = "owl.cs.umanitoba.ca";
    my $port = XXXXX;
    connect( SOCKET, pack_sockaddr_in($port, inet_aton($host)))
        or die "Can't connect to port $port! \n";

    my $line = <SOCKET>;
    print "$line\n";

    # read and send commands over the socket until "E" is entered
    my $client_command = "";

    do{
        print "Type in command: \n>>> ";
        $client_command = <STDIN>;

        send(SOCKET, $client_command, 0);

        $line = <SOCKET>;
        print "$line\n";

    } while( $client_command ne "E" );

    close SOCKET or die "close: $!";

    print "Client finished.\n";
}

StartClient()