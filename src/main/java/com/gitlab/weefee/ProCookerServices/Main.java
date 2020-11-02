package com.gitlab.weefee.ProCookerServices;

import express.Express;
import express.ExpressRouter;

public class Main {
    public static final String version = "0.1-SNAPSHOT";

    public static void main(String[] args) {
        Weekly.initial();
        News.initial();

        Express app = new Express() {{
            // Return server version when accessing root
            get("/", (req, res) -> {
                res.send(version);
            });

            use("/cloudData/", new ExpressRouter(){{
                get("/:playerID", CloudData::getUserData);
                post("/:playerID", CloudData::postUserData);
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
