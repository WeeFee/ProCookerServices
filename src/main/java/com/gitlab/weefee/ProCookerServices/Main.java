package com.gitlab.weefee.ProCookerServices;

import express.Express;

public class Main {
    public static void main(String[] args) {
        Express app = new Express();

        app.get("/", (req, res) -> {
            res.send("Hello World");
        }).listen(6969);
    }
}
