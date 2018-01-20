import pyautogui
import socket
import time
import struct
import os

pyautogui.FAILSAFE = False

HOST = "100.64.135.133"
PORT = 12345

'''
0 = move the mouse -> uses sockets to get the new coordinates
1 = click -> # of clicks; 0 for left and 1 for right
2 = scroll -> pixels moved; 0 for vertical and 0 for horizontal 
3 = hold/select -> it changes the isOn value
4 = center cursor 
5 = changeSensitivity -> 1 for changing scroll's senstivity and 0 for mouse; 1 if multiplier else addition; 1 if decrease; number
6 = write stuff -> just send a string
7 = keyboard keys -> send a string from the array below
8 = shortcuts -> send a string from the array below
9 = open application -> send the name of the application as a string
10 = run commands in terminal -> send a string to be executed
'''


def startClient():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind((HOST, PORT))
    print("Starting client...")
    mouseSensitivity = 3.0       # tolerance for magnetic field fluctuation
    scrollSensitivity = 10.0     # number of clicks to scroll
    isOn = 0

    while True:
        data, addr = s.recvfrom(1024)
        command = int(data[0])
        prevX = 0
        prevY = 0
        prevZ = 0

        # move the mouse
        if command == 0:
            coords = struct.unpack('<3d', data[1:])
            print(coords)
            x = coords[0]
            y = coords[1]
            z = coords[2]
            diffX = x - prevX
            diffY = y - prevY
            diffZ = z - prevZ
            pyautogui.moveRel(diffX * mouseSensitivity, diffZ * mouseSensitivity)
            prevX = x
            prevY = y
            prevZ = z

        # click
        elif command == 1:
            numClicks = int(data[1])
            isRight = (data[2])
            if isRight == 0:
                button = 'left'
            elif isRight == 1:
                button = 'right'
            pyautogui.click(button = button, clicks = numClicks)

        # scroll
        elif command == 2:
            upDown = int(data[1])
            isHor = int(data[2]) # default 0
            if isHor == 0:
                pyautogui.scroll(direction * scrollSensitivity)
            else:
                pyautogui.hscroll(direction * scrollSensitivity)
        
        # hold/select
        elif command == 3:
            if isOn == 0:
                isOn = 1
                pyautogui.mouseDown()
            else:
                isOn = 0
                pyautogui.mouseUp()
            
        # center cursor
        elif command == 4:
            centerX = pyautogui.size()[0] / 2
            centerY = pyautogui.size()[1] / 2
            pyautogui.moveTo(centerX, centerY, 0.2)

        # change sensitivity; Should we include possibility for double/half etc.?
        elif command == 5: 
            isScroll = int(data[1]) # default 0 set by andriod service
            isMult = int(data[2]) 
            isDec  = int(data[3])
            number = int(data[4])
            if isMult == 0:
                if isScroll == 0:
                    mouseSensitivity = mouseSensitivity + number 
                else:
                    scrollSensitivity = scrollSensitivity + number
            else:
                if isDec == 0:
                    if isScroll == 0:
                        mouseSensitivity = mouseSensitivity * number 
                    else:
                        scrollSensitivity = scrollSensitivity * number
                else:
                    if isScroll == 0:
                        mouseSensitivity = mouseSensitivity / number 
                    else:
                        scrollSensitivity = scrollSensitivity / number

        # write stuff
        elif command == 6:
            string = data[1:].decode("utf-8") 
            pyautogui.typewrite(string)
        
        # hotkeys
        # ['accept', 'add', 'alt', 'altleft', 'altright', 'apps', 'backspace', 'browserback', 'browserfavorites', 'browserforward', 'browserhome', 'browserrefresh', 'browsersearch', 'browserstop', 'capslock', 'clear', 'convert', 'ctrl', 'ctrlleft', 'ctrlright', 'decimal', 'del', 'delete', 'divide', 'down', 'end', 'enter', 'esc', 'escape', 'execute', 'f1', 'f10', 'f11', 'f12', 'f2', 'f3', 'f4', 'f5', 'f6', 'f7', 'f8', 'f9', 'final', 'fn', 'hanguel', 'hangul', 'hanja', 'help', 'home', 'insert', 'junja', 'kana', 'kanji', 'launchapp1', 'launchapp2', 'launchmail', 'launchmediaselect', 'left', 'modechange', 'multiply', 'nexttrack', 'nonconvert', 'num0', 'num1', 'num2', 'num3', 'num4', 'num5', 'num6','num7', 'num8', 'num9', 'numlock', 'pagedown', 'pageup', 'pause', 'pgdn', 'pgup', 'playpause', 'prevtrack', 'print', 'printscreen', 'prntscrn', 'prtsc', 'prtscr', 'return', 'right', 'scrolllock', 'select', 'separator', 'shift', 'shiftleft', 'shiftright', 'sleep', 'space', 'stop', 'subtract', 'tab', 'up', 'volumedown', 'volumemute', 'volumeup', 'win', 'winleft', 'winright', 'yen', 'command', 'option', 'optionleft', 'optionright']
        elif command == 7:
            operation = data[1:].decode("utf-8") 
            pyautogui.keyDown(operation)
        
        # keyboard shortcuts
        # ['copy', 'paste', 'cut', 'undo', 'redo', 'tabs', 'desktopright', 'desktopleft', 'desktopnew', 'gohome']
        elif command == 8:
            operation = data[1:].decode("utf-8")
            if operation == 'copy':
                pyautogui.hotkey('ctrl', 'c')
            elif operation == 'find':
                pyautogui.hotkey('ctrl', 'f')
            elif operation == 'paste':
                pyautogui.hotkey('ctrl', 'v')
            elif operation == 'cut':
                pyautogui.hotkey('ctrl', 'x')
            elif operation == 'undo':
                pyautogui.hotkey('ctrl', 'z')
            elif operation == 'redo':
                pyautogui.hotkey('ctrl', 'y')
            elif operation == 'tabs':
                pyautogui.hotkey('win', 'tab')
            elif operation == 'desktopright':
                pyautogui.hotkey('win', 'shift', 'right')
            elif operation == 'desktopleft':
                pyautogui.hotkey('win', 'shift', 'left')
            elif operation == 'desktopnew':
                pyautogui.hotkey('win', 'ctrl', 'd')
            elif operation == 'gohome':
                pyautogui.hotkey('win', 'd')
        
        # open application
        # eg. ['chrome', 'chromium']
        elif command == 9:
            operation = data[1:].decode("utf-8")
            if os.name == 'nt': #Windows
                operation = "start " + operation
                os.system(operation) 
            elif os.name == 'posix': #Linux/macOS
                os.system(operation)

        # run commands in terminal
        elif command == 10:
            operation = data[1:].decode("utf-8")
            os.system(operation)

    s.close()

startClient()
