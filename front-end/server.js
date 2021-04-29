const bodyParser = require("body-parser");
const cors = require("cors");
const express = require("express");
const path = require("path");
const fs = require('fs');
const app = express();
const login = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cors());
login.use(cors());


login.use("/static", express.static(path.resolve(__dirname, "frontend", "static")));
login.get("/*", (req, res) => {
    res.sendFile(path.resolve(__dirname, "frontend", "login.html"));
});

login.listen(process.env.PORT || 8280, () => console.log("Login running..."));

app.use("/static", express.static(path.resolve(__dirname, "frontend", "static")));

app.get("/*", (req, res) => {
    res.sendFile(path.resolve(__dirname, "frontend", "index.html"));
});

app.listen(process.env.PORT || 8180, () => console.log("Server running..."));