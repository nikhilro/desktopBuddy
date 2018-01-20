import pyautogui
import socket
import time
import struct

HOST = "localhost"
PORT = 10001

MOUSE_SENSITIVITY = 3.0       # tolerance for magnetic field fluctuation
SCROLL_SENSITIVITY = 10.0     # number of clicks to scroll

def startClient():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind((HOST, PORT))1
    print("Starting client...")

    
    prevX = 0.0
    prevY = 0.0
    prevZ = 0.0


    while True:
        data, addr = s.recvfrom(1024)
        command = int(data[0])

        # move the mouse
        if command == 0:
            coords = struct.unpack('<3f', data[1:])
            print(coords)
            x = coords[0]
            y = coords[1]
            z = coords[2]
            diffX = x - prevX
            diffY = y - prevY
            diffZ = z - prevZ
            pyautogui.moveRel(diffX * MOUSE_SENSITIVITY, diffZ * MOUSE_SENSITIVITY)
            prevX - x
            prevY = y
            prevZ = z

        # click
        elif command == 1:
            numClicks = int(data[1])
            buttonNum = int(data[2])
            if buttonNum == 0:
                button = 'left'
            elif buttonNum == 1:
                button = 'right'
            pyautogui.click(button = button, clicks = numClicks)

        #

        # scroll
        elif command == 2:
            direction = int(data[1])
            pyautogui.scroll(direction * SCROLL_SENSITIVITY)
        
        # center cursor
        elif command == 3:
            centerX = pyautogui.size()[0] / 2
            centerY = pyautogui.size()[1] / 2
            pyautogui.moveTo(100, 100, 0.2)

        # type
        elif command == 4:
            pyautogui.typewrite('Hello world!', interval = 0.1)

    s.close()

startClient()
