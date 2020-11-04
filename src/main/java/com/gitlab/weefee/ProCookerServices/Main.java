/* SPDX-License-Identifier: GPL-3.0-or-later */

package com.gitlab.weefee.ProCookerServices;

import express.Express;
import express.ExpressRouter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final String version = "0.1-SNAPSHOT";

    public static Database database;

    public static void main(String[] args) {
        database = new Database("./db/");

        Runnable databaseFlush = new Runnable() {
            public void run() {
                System.out.println("Saving Database");
                database.flushDatabase();
            }
        };
        ScheduledExecutorService execDatabaseFlush = Executors.newScheduledThreadPool(1);
        execDatabaseFlush.scheduleAtFixedRate(databaseFlush, 25, 25, TimeUnit.MINUTES);

        Express app = new Express() {{
            // Return server version when accessing root
            get("/", (req, res) -> res.send(version));

            use("/cloudData/", new ExpressRouter(){{
                get("/:playerID", CloudData::getUserData);
                post("/:playerID", CloudData::postUserData);
            }});

            use("/leaderboard/", new ExpressRouter(){{
                get("/:leaderboardEntry", Leaderboard::getLeaderboard);
                post("/:leaderboardEntry", Leaderboard::postLeaderboard);
            }});

            use("/weekly", new ExpressRouter(){{
                get("/", Weekly::getCurrentWeekly);
            }});

            use("/news", new ExpressRouter(){{
                get("/", News::getCurrentNews);
            }});

            listen(6969);
        }};
    }
}
