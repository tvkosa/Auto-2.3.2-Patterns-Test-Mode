package ru.netology.testmode;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import java.util.Locale;
import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(UserForRegistration user) {
        Gson gson = new Gson();
        given()
                .spec(requestSpec)
                .body(gson.toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().firstName();
    }

    public static String getRandomPassword() {
        return faker.animal().name();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserForRegistration getUser(String status) {
            return new UserForRegistration(getRandomLogin(), getRandomPassword(), status);
        }

        public static UserForRegistration getRegisteredUser(String status) {
            UserForRegistration registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class UserForRegistration {
        String login;
        String password;
        String status;
    }
}