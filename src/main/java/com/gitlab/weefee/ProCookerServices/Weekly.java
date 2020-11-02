package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

public class Weekly {
    private static String currentWeekly = "";

    // Populate frequently accessed variables for Weekly
    public static void initial() {
        currentWeekly = Main.database.readFromDatabase("info", "weekly");
    }

    public static void getCurrentWeekly(Request req, Response res) {
        res.send(currentWeekly);
    }
}
