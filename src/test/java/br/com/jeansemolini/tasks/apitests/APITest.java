package br.com.jeansemolini.tasks.apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given() //dado
        .when() //quando
            .get("/todo")
        .then() //então
            .statusCode(200);
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured.given() //dado
            .body("{ \"task\": \"Teste da API\", \"dueDate\": \"2030-12-08\" }")
            .contentType(ContentType.JSON)
        .when() //quando
            .post("/todo")
        .then() //então
            .statusCode(201);
    }

    @Test
    public void naoDeveAdicionarTarefaInvalida() {
        RestAssured.given() //dado
            .body("{ \"task\": \"Teste da API\", \"dueDate\": \"2010-12-08\" }")
            .contentType(ContentType.JSON)
        .when() //quando
            .post("/todo")
        .then() //então
            .statusCode(400)
            .body("message", CoreMatchers.is("Due date must not be in past"));
    }

    @Test
    public void deveRemoverTarefaComSucesso() {
        //inserir
        Integer id = RestAssured.given() //dado
            .body("{ \"task\": \"Tarefa para remoção\", \"dueDate\": \"2030-12-08\" }")
            .contentType(ContentType.JSON)
        .when() //quando
            .post("/todo")
        .then() //então
            .statusCode(201)
            .extract().path("id");

        //remover
        RestAssured.given()
        .when()
            .delete("/todo/" + id)
        .then()
            .statusCode(204);
    }
}


