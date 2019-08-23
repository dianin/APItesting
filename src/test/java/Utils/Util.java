package Utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;

public class Util {

    public static int getStatusCode (Response response)
    {
        return response.getStatusCode();
    }

    public static ArrayList getPeopleList (JsonPath json)
    {
        return json.get("data.id");
    }




}
