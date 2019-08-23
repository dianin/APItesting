package Tests;

import Utils.RestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.generator.qdox.model.annotation.AnnotationLessThan;
import org.jsoup.select.Evaluator;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static Utils.RestUtil.getJsonPath;
import static Utils.RestUtil.getResponse;
import static Utils.Util.getPeopleList;
import static Utils.Util.getStatusCode;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;

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







}
