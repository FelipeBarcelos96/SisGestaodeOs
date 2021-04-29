var http = require('http');
var url = require('url');
var express = require('express'); // requre the express framework
var path = require('path')
var fs = require('fs');
const request = require('request');
const { response } = require('express');
const { json } = require('body-parser');


var app = express();

app.use(express.static("view"));

const requestUrl = url.parse(url.format({
    protocol: 'http',
    hostname: 'localhost',
    pathname: '/api/users',
    port: '8080',
    query: {
        id: '1'
    },
    method: 'GET'
}));

var user;

callback = function(response) {
    var str = ''
    response.on('data', function(chunk) {
        str += chunk;
    });

    response.on('end', function() {
        user = str;
        user = JSON.parse(user);
    });
}

var req = http.request(requestUrl, callback);
//This is the data we are posting, it needs to be a string or a buffer
//req.write("hello world!");
req.end();



console.log(requestUrl);

/*
var menu;

app.get("/", (req, res) => {
    res.sendFile('index.html');
}); 
*/

/*

var server = http.createServer(app);

server.listen(8180, function() {
    var host = server.address().address
    var port = server.address().port
        //console.log("app listening at http://%s:%s", host, port)
});
*/



var server = http.createServer(function(request, response) {
    response.writeHeader(200, { "Content-Type": "application/json" });
    // response.writeHeader(200, { "Content-Type": "text/html" });
    // response.write(JSON.stringify(user));
    response.write(JSON.stringify(user));
    response.end();
}).listen(8280, function() {
    var host = server.address().address
    var port = server.address().port
    console.log("REST API demo app listening at http://%s:%s", host, port)
});