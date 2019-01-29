package com.github.gerivansantos.config.route

import com.github.gerivansantos.controller.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path

class RouteConfig(private val userController: UserController){

    fun registerRoute(app: Javalin){
        app.routes{
            path("api"){
                userController.registerResources()
            }

        }
    }

}