package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

public class News {
    private static String currentNews = "";

    // Populate frequently accessed variables for News
    public static void initial() {
        currentNews = Main.database.readFromDatabase("info", "news");
    }

    public static void getCurrentNews(Request req, Response res) {
        res.send(currentNews);
    }
}
