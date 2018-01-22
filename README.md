# Desktop Buddy

An Android background service to remotely control desktop input
using Google Assistant voice commands and orientation data from sensors.

## How we built it
   Google assistant action -> Dialogflow -> Firebase -> Android background service -> Python server on the desktop 
  
## Challenges we ran into
  1. We started out with using Cordova to get magnetic readings and then had to switch to Java due to inability to send requests.
  2. Google Assitant is incapable of local fulfillment so we had to add an unnecessary step of going through Firebase.
  3. Firebase + databases = lag.
  4. Sensors in phones are not the most accurate and can lead to jitter in mouse movements.
  5. We couldn't reliably deduce the position of the magnet from the magnetic field strength. 
  
## Accomplishments that we're proud of
  1. Forwaring pipeline was fast enough for negligible control latency.
  2. Seamless integration of all the parts.
