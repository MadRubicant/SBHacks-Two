import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Created by john on 4/23/16.
 */
public class Server {
    public Server(){

    }

    public void setup(){
        System.out.println("Starting server.");

        post("/identify", (req, res) -> {
            return handlePost(req, res);
        });

        System.out.println("Server setup complete.");
    }

    public static String handlePost(Request req, Response res){

        return "Post handled";
    }
}
