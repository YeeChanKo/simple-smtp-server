var util = require('util');
var TCPClient = require('simpletcp').client();
var readline = require('readline');

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// create a connection to localhost:8080
var client = new TCPClient('client1', 'localhost', 587,
    function () {
        rl.on('line', (line) => {
            client.write(line);
        });
        // client.close();
    }
);

client.on('data', function (obj) {
    console.log(obj);
});

// open the connection
client.open();
