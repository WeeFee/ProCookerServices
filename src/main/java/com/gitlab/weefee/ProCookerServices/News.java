package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

public class News {
    public static void getCurrentNews(Request req, Response res) {
        res.send(Main.database.readFromDatabase("info", "news"));
    }
}
