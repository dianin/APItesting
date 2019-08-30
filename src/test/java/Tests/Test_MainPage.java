package Tests;

import Models.User;
import Utils.RestUtil;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import java.util.List;


import static Utils.RestUtil.*;
import static Utils.Util.getPeopleList;
import static Utils.Util.getStatusCode;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.hamcrest.Matchers;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.hamcrest.Matchers.lessThan;
import static org.assertj.core.api.Assertions.assertThat;


public class Test_MainPage {

    private Response response = null;
    private JsonPath json = null;

    @BeforeTest
    public void setUp()
    {
        RestUtil.setBaseURI("https://reqres.in");
        RestUtil.setBasePath("/api/users");
        RestUtil.setContentType(ContentType.JSON);
        RestUtil.createSearchQueryPath("page", "2");
        response = getResponse();
        json = getJsonPath(response);
    }

    @Test
    public void checkStatus ()
    {
        Assert.assertEquals(getStatusCode(response), 200);

    }

    @Test
    public void checkForUserEmail ()
    {
        Assert.assertEquals(json.get("data[0].email"),("michael.lawson@reqres.in"));
    }

    @Test
    public void verifyOnlySixPeopleReturned ()
    {
        Assert.assertEquals(getPeopleList(json).size(), 6);
    }

    @Test
    public void verifyTimeToResponse ()
    {
        response.then().time(lessThan(2L), SECONDS);
    }

    @Test
    public void verifyJsonBody ()
    {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("peoples.json"));
    }

    @Test
    public void put_test() {
        Response response1 = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" +
                        "\"id\": \"2\"," +
                        "\"name\": \"Lisa Tamaki\"," +
                        "\"job\": \"20000\"}")
                .when()
                .put(getBaseURI() + "/api/users/2");
        System.out.println("PUT Response\n" + response1.asString());
        // tests
        response1.then().body("id", Matchers.is("2"));
        response1.then().body("name", Matchers.is("Lisa Tamaki"));
        response1.then().body("job", Matchers.is("20000"));
    }

    @Test
    public void put_test2()
    {
        baseURI = "https://reqres.in/api/users/2";
        RequestSpecification request = RestAssured.given();

        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("name","Lisa Tamaki");
        requestParams.addProperty("job","Lisa 20000");
        request.body(requestParams.toString());

        Response response1 = request.post();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, "201");
        String successCode = response.jsonPath().get("SuccessCode");
        Assert.assertEquals( "Correct Success code was returned", successCode, "OPERATION_SUCCESS");
        System.out.println(response1.toString());
    }


    @Test
    public void addUser ()
    {
        RequestSpecification request = RestAssured.given().contentType(ContentType.JSON);

        User expected = generateNewUser();

        Response createUserResponse = request.body(expected).post("https://reqres.in/api/users");

        createUserResponse.then().assertThat().statusCode(201);

        expected.id = createUserResponse.jsonPath().getInt("id");

        User actual = request.get("https://reqres.in/api/users"+expected.id).as(User.class);

        assertThat(expected).isEqualToComparingFieldByField(actual);

        List<User> allUsers =request.get("https://reqres.in/api/users").jsonPath().getList("", User.class);
        assertThat(allUsers)
                .extracting("name", "job")
                .contains(tuple(expected.getName(), expected.getJob()));







    }

    public  User generateNewUser ()
    {
        User user;
        String name = RandomStringUtils.randomAlphabetic(5);
        String job = RandomStringUtils.randomAlphabetic(5);
       return user = new User(name, job);
    }







}
