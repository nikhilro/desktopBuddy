import pyautogui
import socket
import time

host = "100.64.135.133"
port = 10001

def startClient():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host, port))
    print("Starting connection...")
    s.listen(5)
    conn, addr = s.accept()
    print("Got connection from", addr)

    while True:
        data = conn.recv(1024)
        for ch in data:
            print('{}\n'.format(ord(ch)))
            
        #TODO parse data here
        # scroll
        # move cursor
        # left click, right click, double click
        # center cursor
        # type

    s.close()

startClient()
