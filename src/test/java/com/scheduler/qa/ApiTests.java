package com.scheduler.qa;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTests {
    @Test
    public void loginUserTest() throws IOException {
        String response = apiLoginOrRegistration("userQA23Lena_api@gmail.com", "Aa1234567");
       // System.out.println(response);

        JsonElement parsed = new JsonParser().parse(response);
        JsonElement token = parsed.getAsJsonObject().get("token");
        JsonElement status = parsed.getAsJsonObject().get("status");
        JsonElement registration = parsed.getAsJsonObject().get("registration");
        System.out.println(token);
        System.out.println(status);
        System.out.println(registration);

        Assert.assertFalse(registration.getAsBoolean());
        Assert.assertEquals(status.toString(), "\"Login success\"");
    }

@Test
    public void loginUserTestCodeResponse() throws IOException {
        String email = "userQA23Lena_api@gmail.com";
        String password = "Aa1234567";
        int statusCode = sendPostRequest("/api/login")
                .bodyString("{\"email\": \""+ email + "\",\"password\": \""+ password +"\"}", ContentType.APPLICATION_JSON)
                .execute().returnResponse().getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, 200);
    }

    @Test
    public void getRecordsPeriod() throws IOException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InVzZXJRQTIzTGVuYV9hcGlAZ21haWwuY29tIn0.GYQ1VSnCHRD1KwS-j2EmzjoH-XVvlkw43C1eJTeTfAA"
        String res = Request.Get("http://super-scheduler-app.herokuapp.com/api/recordsPeriod")
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .execute().returnContent().asString();
        System.out.println(res);

        JsonElement parsed = new JsonParser().parse(res);
        String monthFrom = parsed.getAsJsonObject().get("monthFrom").toString();
        String monthTo = parsed.getAsJsonObject().get("monthTo").toString();
        String yearFrom = parsed.getAsJsonObject().get("yearFrom").toString();
        String yearTo = parsed.getAsJsonObject().get("yearTo").toString();

        String records = sendPostRequest("/api/records").addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .bodyString("{\"monthFrom\": " + monthFrom + ", \"monthTo\": " + monthTo + ", \"yearFrom\": " + yearFrom + ", \"yearTo\": " + yearTo + "}", ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();

        System.out.println(records);

    }

    private String apiLoginOrRegistration(String email, String password) throws IOException {
        return sendPostRequest("/api/login")
                .bodyString("{\"email\": \""+ email + "\",\"password\": \""+ password +"\"}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
    }

    public Request sendPostRequest(String controller) {
        return Request.Post("http://super-scheduler-app.herokuapp.com"+ controller);
    }
}
