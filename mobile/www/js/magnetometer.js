var magnitudeText = document.getElementById('magnitude');
var coordinates = document.getElementById('coordinates');
var lastReading = { x: 0, y: 0, z: 0, magnitude: 0 };
var avgField = { x: 0, y: 0, z: 0, magnitude: 0 };
var position = { x: 0, y: 0, z: 0, magnitude: 0 };
var interval = 0;

function displayReadings() {
    // calculations
    var reading = lastReading;
    var x = reading.x - avgField.x;
    var y = reading.y - avgField.y;
    var z = reading.z - avgField.z;
    var magnitude = reading.magnitude - avgField.magnitude;
    // display numbers
    magnitudeText.innerText = [magnitude.toFixed(3), 'μT'].join('');
    coordinates.innerText = ['X: ', x.toFixed(3),
                           ', Y: ', y.toFixed(3),
                           ', Z: ', z.toFixed(3)].join('');
}

function watchMagnetometer() {
    if (window.cordova && cordova.plugins && cordova.plugins.magnetometer) {
        cordova.plugins.magnetometer.watchReadings(
            function success (reading) {
                // collect readings to three decimal places
                lastReading = {
                    x: reading.x,
                    y: reading.y,
                    z: reading.z,
                    magnitude: reading.magnitude
                };
            },
            function error (err) {
                alert(err);
            }
        );
    }
    else {
        alert('cordova = ' + !!window.cordova);
        alert('cordova.plugins = ' + !!cordova.plugins);
        alert('cordova.plugins.magnetometer = ' + !!cordova.plugins.magnetometer);
    }
}

function calibrateMagnetometer() {
    var iterations = 0;
    var cInterval = window.setInterval(function() {
        avgField.x += lastReading.x;
        avgField.y += lastReading.y;
        avgField.z += lastReading.z;
        avgField.magnitude += lastReading.magnitude;
        iterations++;
    }, 100);
    window.setTimeout(function() {
        avgField.x /= iterations;
        avgField.y /= iterations;
        avgField.z /= iterations;
        avgField.magnitude /= iterations;
        window.clearInterval(cInterval);
        interval = window.setInterval(displayReadings, 100);
    }, 5000);
}

//
//Distance = (1/sqrt(bx^2+by^2+bz^2))^1/3 where b is the magnetic field. Formula based on observations done by Shun and gang (thanks m8)
//
function getPosition(){
    var tempDis = Math.pow(avgField.x**2 + avgField.y**2 + avgField.z**2, 1/2); 
    if (tempDis != 0){
        var dist = 1/ Math.pow(avgField.x**2 , avgField.y**2 , avgField.z**2 , 1/3);
        position.x = Math.cos(avgField.x/tempDis)*dist;
        position.y = Math.cos(avgField.y/tempDis)*dist;
        position.z = Math.cos(avgField.z/tempDis)*dist;
    } else {
        alert("tempDis == 0");
    }
}

function stop() {
    window.clearInterval(interval);
    if (window.cordova && cordova.plugins && cordova.plugins.magnetometer) {
        cordova.plugins.magnetometer.stop();
    }
}

function initMagnetometer() {
    watchMagnetometer();
    calibrateMagnetometer();
}

document.addEventListener("pause", stop, false);
document.addEventListener("resume", watchMagnetometer, false);
document.addEventListener("deviceready", initMagnetometer, false);
