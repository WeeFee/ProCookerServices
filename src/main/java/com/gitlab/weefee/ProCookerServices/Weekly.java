package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Weekly {
    private static String currentWeekly = "";

    // Populate frequently accessed variables for Weekly
    public static void initial() {
        File weeklyObj = new File("./db/weekly");
        try {
            Scanner weeklyReader = new Scanner(weeklyObj);
            while (weeklyReader.hasNextLine()) {
                currentWeekly = weeklyReader.nextLine();
            }
            weeklyReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getCurrentWeekly(Request req, Response res) {
        res.send(currentWeekly);
    }
}
