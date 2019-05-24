package com.chancellor.api

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import model.OrderRequest
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val mDataBase = DataBase()

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(StatusPages){
        exception<Throwable>{e ->
            call.respondText ( e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    routing {
        /*get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }*/

        post("/api/registration"){
            val parameters: Parameters = call.receiveParameters()

            val user = User(
                mDataBase.getAllUsers().size,
                parameters["phone"]!!, parameters["email"]!!, parameters["firstName"]!!,
                parameters["secondName"]!!, parameters["address"]!!, parameters["password"]!!
            )

            if (mDataBase.addUser(user)){
                call.respond(DefaultResponse(data = user))
            }else{
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse(message = "User already exists."))
            }

        }

        post("api/signin"){
            val parameters: Parameters = call.receiveParameters()
            val phone = parameters["phone"]
            val password = parameters["password"]

            val user = mDataBase.getAllUsers().find { phone == it.phone && password == it.password }

            if (user != null){
                call.respond(DefaultResponse(data = user))
            }else{
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse(message = "User is not exists."))
            }

        }

        get("api/getCategories"){
            val response = File("src/database/getCategories.json").readText()
            call.respondText(response, ContentType.Application.Json)
        }

        get ("api/getCatalogById/{id}"){
            val id = call.parameters["id"]

            if (id == "234"){
                val response = File("src/database/getCatalogById.json").readText()
                call.respondText(response, ContentType.Application.Json)
            }else{
                val response = "{\"success\": true, \"data\": []}"
                call.respondText(response, ContentType.Application.Json)
            }
        }

        get("api/getCatalogs"){
            val response = File("src/database/getCatalogs.json").readText()
            call.respondText(response, ContentType.Application.Json)
        }

        get("api/getHistory/{userId}"){
            val response = File("src/database/getHistory.json").readText()
            call.respondText(response, ContentType.Application.Json)
        }

        post("api/editProfile"){
            val parameters: Parameters = call.receiveParameters()
            val userId = parameters["userId"]!!.toInt()
            val user = mDataBase.getUserById(userId)
                .copy(phone =  parameters["phone"]!!, email = parameters["email"]!!,
                    firstName = parameters["firstName"]!!, secondName = parameters["secondName"]!!,
                    address = parameters["address"]!!)
            mDataBase.getAllUsers()[userId] = user

            val response = "{\"success\":true}"
            call.respondText(response, ContentType.Application.Json)
        }

        post("api/checkout"){
            val request = call.receive<OrderRequest>()
            val response = "{\"success\":true}"
            call.respondText(response, ContentType.Application.Json)
        }
    }
}

