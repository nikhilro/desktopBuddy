function sendData() {
  dataString = "hello";
  var data = new Uint8Array(dataString.length);
  for (var i = 0; i < data.length; i++) {
    data[i] = dataString.charCodeAt(i);
  }
  socket.write(data);
  alert(data);
}

function connectServer() {
  var socket = new Socket();
  socket.open(
    "100.64.135.133",
    10001,
    sendData,
    function(errorMessage) {
      alert(errorMessage);
    }
  );
}

document.addEventListener('deviceready', connectServer, false);

