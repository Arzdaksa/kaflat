
const express = require("express");
const app = express();

app.get("/test/", (req, res) => {
    res.status(200).send("Greetings from Node JS Express server shell !");
});

app.get("/test/greeting", (req, res) => {
    res.status(201).send("Hello " + req.query.name + " " + req.query.last.toUpperCase());
});

app.listen(9090, () => {
    console.log("Server listening on port 9090...\n");
});

