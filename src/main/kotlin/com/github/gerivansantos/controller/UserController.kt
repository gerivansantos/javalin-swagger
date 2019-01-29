package com.github.gerivansantos.controller

import com.github.gerivansantos.controller.UserController.Companion.USERS_PATH
import io.javalin.Context
import io.javalin.apibuilder.ApiBuilder.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import javax.ws.rs.*

@Path(USERS_PATH)
@Tag(name = "Users API v1.0.0")
class UserController(){

    companion object {
        const val USERS_PATH = "/users"
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Creates a new user")
    fun createUser(username: UserDTO){
    }



    @GET
    @Path("{id}")
    @Operation(summary = "Search a user")
    @Produces("application/json")
    @RequestBody(description = "Pet object that needs to be added to the store", required = true,
        content = arrayOf(
            Content(
                schema = Schema(implementation = UserDTO::class)
            )
        )
    )
    fun getUser(
        @Parameter(hidden = true) ctx:Context){
        val userId = ctx.pathParam("id")
        ctx.result("GET USER - $userId")

    }

    fun registerResources() {
        path(USERS_PATH) {
            post{ctx -> this.createUser(UserDTO(1, "Teste"))}
            path(":id")
            {
                get {
                        ctx -> this.getUser(ctx)
                    }
            }
        }
    }

}