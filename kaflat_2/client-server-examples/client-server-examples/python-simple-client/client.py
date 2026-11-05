import socket
import sys

HOST = 'localhost'
PORT = 2323

with socket.create_connection((HOST, PORT)) as sock:
    print(f'Connected to server at {HOST}:{PORT}')
    for line in sys.stdin:
        sock.sendall((line.rstrip('\n') + '\n').encode())
        response = sock.recv(4096)
        print('Server: ' + response.decode().strip())
