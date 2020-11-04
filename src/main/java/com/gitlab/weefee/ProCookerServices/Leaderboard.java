/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import express.http.request.Request;
import express.http.response.Response;
import express.utils.Status;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard {
    /**
     *
     * @param req
     * @param res
     */
    public static void getLeaderboard(Request req, Response res) {
        res.send(Main.database.readFromDatabase("leaderboards", req.getParam("leaderboardEntry")));
    }

    /**
     *
     * @param req
     * @param res
     */
    public static void postLeaderboard(Request req, Response res) {
        String reqBody = new BufferedReader(new InputStreamReader(req.getBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        String[] newEntry = reqBody.split("\\|");

        if (!(reqBody.split("\\|").length == 2)) {
            res.sendStatus(Status._400);
            return;
        }  else if (reqBody.split("\\|")[0].equals("")) {
            res.sendStatus(Status._403);
            return;
        }

        String[] currentLeaderboard = Main.database.
                readFromDatabase("leaderboards", req.getParam("leaderboardEntry"))
                .split(",");

        List<String> currentLeaderboardList = Arrays.asList(currentLeaderboard);

        StringBuilder newLeaderboard = new StringBuilder();



        for (int i = 0; i < 5; i++) {
            try {
                newLeaderboard.append(currentLeaderboard[i] + ",");
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        if(Main.database.writeToDatabase("leaderboards", req.getParam("leaderboardEntry"), newLeaderboard.toString())) {
            res.sendStatus(Status._200);
        } else {
            res.sendStatus(Status._500);
        }
    }
}
