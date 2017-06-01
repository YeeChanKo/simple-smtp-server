TCPServer = require('simpletcp').server();
LineConnection = require('simpletcp').LineConnection();
require('util').inherits(Connection, LineConnection);

function Connection(server, socket) {
    Connection.super_.call(this, server, socket);
    this.on('data', function (obj) {
        console.log(obj);
    });
}

// called when a new connection has started
Connection.prototype.start = function () {
    this.write("feed me"); // tell client we are alive
};

var server = new TCPServer('smtp', 587, Connection);

server.on('listen', function () {
    console.log("Server started");
});

server.on('connection', function (conn) {
    conn.start();
});

server.on('error', function (err) {
    console.log('Trying to start server Got error: ' + err);
    server.destroy();
});

server.start();
