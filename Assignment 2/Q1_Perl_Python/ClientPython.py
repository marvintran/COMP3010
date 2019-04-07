
#  Name: Marvin Tran
#  Student Number: XXXXXXX
#  Professor: Peter Graham
#  COMP 3010 - Distributed Computing - Winter 2019
#  Assignment 2 Question 1

import socket


def build_message(client_socket):
    end_of_line = False
    server_sentence = ''

    # build the welcome message received from the server
    while not end_of_line:
        data_received = client_socket.recv(1)  # read in one byte at a time
        if data_received != '\n' and data_received != '':
            server_sentence += data_received
        else:
            end_of_line = True  # \n signals the end of a line
    print(server_sentence + '\n')


def start_client():

    print('Client starting.')

    # Create a socket object and connect to it
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    host = 'owl.cs.umanitoba.ca'
    port = XXXXX
    client_socket.connect((host, port))

    # after successful connection, read welcome message from server
    build_message(client_socket)

    # read and send commands over the socket until "E" is entered
    client_command = ''

    while client_command != 'E':
        client_command = raw_input('Type in command: \n>>> ')
        client_socket.send(client_command + '\n')

        # after sending command, get return message
        build_message(client_socket)

    client_socket.close()  # Close the socket when done

    print('Client finished.')


start_client()
