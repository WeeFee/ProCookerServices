package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

public class Weekly {
    public static void getCurrentWeekly(Request req, Response res) {
        res.send(Main.database.readFromDatabase("info", "weekly"));
    }
}
