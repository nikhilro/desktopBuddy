# Desktop Buddy

An Android background service to remotely control desktop input using voice commands via Google Assistant and orientation data from rotation sensors.

## How we built it
   Google Assistant/Actions SDK -> Dialogflow -> Firebase -> Android Background Service -> Python Desktop Client 
  
## What it does
   Allows users to control keyboard and mouse input by speaking commands and tilting their phone. Features browser functionality such as switching tabs, opening Google Drive and Gmail and media controls such as fullscreen, play/pause, and volume control.
  
## Challenges we ran into
  1. We started out with using Cordova to get magnetometer readings and then had to switch to Java due to inability to send requests.
  2. We couldn't reliably deduce the position of the magnet from the magnetic field strength and had to use the rotation sensor instead.
  3. Google Assistant is incapable of local fulfillment so we had to add an unnecessary step of going through Firebase.
  4. Firebase + databases = lag.
  5. Sensors in phones are not the most accurate and can lead to jitter in mouse movements.
  
## Accomplishments that we're proud of
  1. Forwaring pipeline was fast enough for negligible control latency.
  2. Seamless integration of all the parts.
  3. Getting mouse control to work accurately.
  
## What's next for Desktop Buddy?
   * Adding more voice commands
   * Creating an actual desktop service application with more functionality such as adding or customizing voice commands
