# Desktop Buddy

An android background service to remotely control desktop input using voice commands via Google Assistant and the built-in orientation derived from Sensor fusion.

## Inspiration
   From realms beyond us 
   
## What it does
   Everything a man could dream of ;)
   
## How we built it
   Google assistant action -> Dialogflow -> Firebase -> Android background service -> Python server on the desktop 
  
## Challenges we ran into
  1. Using Cordova to get magnetic readings and then scraping it all cause Cordova doesn't like Node
  2. Google Assitant is incapable of local fulfillment so we had to add a unnecessary step of going through Firebase
  3. Firebase </3 databases
  4. Sensors in phones are not the most accurate and can lead to jitter in mouse movements
  5. We couldn't get the position of the magnet from the magnetic field strength accurately enough 
  
## Accomplishments that we're proud of
  1. Webhooks worked beautifully <3
  2. Integration of all the parts 
  
## What we learned
  Google is good to consumers but their dev options are kinda shiet (Top 10 Anime Betrayals)
  
## What's next for Desktop Buddy
  Gonna wait for Google to implement local fulfillment in Assistant
  
## How do we feel?
  Lacking of fruits :)
