package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class News {
    private static String currentNews = "";

    // Populate frequently accessed variables for News
    public static void initial() {
        File newsObj = new File("./db/news");
        try {
            Scanner newsReader = new Scanner(newsObj);
            while (newsReader.hasNextLine()) {
                currentNews = newsReader.nextLine();
            }
            newsReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getCurrentNews(Request req, Response res) {
        res.send(currentNews);
    }
}
