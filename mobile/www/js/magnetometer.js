var magnitudeText = document.getElementById('magnitude');
var coordinates = document.getElementById('coordinates');
var lastReading = { x: 0, y: 0, z: 0, magnitude: 0 };
var interval = 0;

function displayReadings() {
    // calculations
    var reading = lastReading;
    var x = reading.x;
    var y = reading.y;
    var z = reading.z;
    var magnitude = reading.magnitude;
    // display numbers
    magnitudeText.innerText = [magnitude, 'μT'].join('');
    coordinates.innerText = ['X: ', x, ', Y: ', y, ', Z: ', z].join('');
}

function watchMagnetometer() {
    if (window.cordova && cordova.plugins && cordova.plugins.magnetometer) {
        cordova.plugins.magnetometer.watchReadings(
            function success (reading) {
                // collect readings to three decimal places
                lastReading = {
                    x: reading.x.toFixed(3),
                    y: reading.y.toFixed(3),
                    z: reading.z.toFixed(3),
                    magnitude: reading.magnitude.toFixed(3)
                };
            },
            function error (err) {
                alert(err);
            }
        );
        // refresh rate (ms)
        interval = window.setInterval(displayReadings, 100);
    }
    else {
        alert('cordova = ' + !!window.cordova);
        alert('cordova.plugins = ' + !!cordova.plugins);
        alert('cordova.plugins.magnetometer = ' + !!cordova.plugins.magnetometer);
    }
}

function calibrateReadings(x, y, z) {
  // fancy algorithm to convert sensor data to relative cursor movement
}

function stop() {
    window.clearInterval(interval);
    if (window.cordova && cordova.plugins && cordova.plugins.magnetometer) {
        cordova.plugins.magnetometer.stop();
    }
}

document.addEventListener("pause", stop, false);
document.addEventListener("resume", watchMagnetometer, false);
document.addEventListener("deviceready", watchMagnetometer, false);
