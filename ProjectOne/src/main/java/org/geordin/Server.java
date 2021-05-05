package org.geordin;

import io.javalin.Javalin;

public class Server {

    public static void main(String[] args) {


//create server
        Javalin app = Javalin.create(config->config.enableCorsForAllOrigins()).
                start(9000);
        //maybe remove config..

        //GET



        //POST

        //PUT

        //patch?

        //delete?

    }

}
