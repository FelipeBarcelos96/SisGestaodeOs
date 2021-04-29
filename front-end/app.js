const express = require("express");
const app = express();
app.use(express.static("frontend"));

app.get("/", (req, res) => {
    res.sendFile('index.html');
})

app.listen(8180, function() {
    console.log("Running on port 8180.");
});