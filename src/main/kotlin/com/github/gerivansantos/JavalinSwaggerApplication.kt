package com.github.gerivansantos

import com.github.gerivansantos.config.koin.KoinModuleConfig
import com.github.gerivansantos.config.route.RouteConfig
import com.github.gerivansantos.constants.ApplicationConstants
import com.github.gerivansantos.swagger.swagger
import io.javalin.Javalin
import io.javalin.JavalinEvent
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject

@OpenAPIDefinition(
    info = Info(
                title = "SuperAPI",
                version = "1.0.0",
                description = "My API",
                license = License(
                    name = "Apache 2.0",
                    url = "http://foo.bar"
                ),
                contact = Contact(
                    url = "http://google.com",
                    name = "Gerivan Santos",
                    email = "gerivantpc3@gmail.com"
                )
    ),
    servers = [
                Server(
                        description = "Meu server",
                        url = "http://localhost:${ApplicationConstants.DEFAULT_SERVER_PORT}/api"

                )]

)
class JavalinSwaggerApplication() : KoinComponent{

    private val routeConfig: RouteConfig by inject()

    fun init(): Javalin {

        startKoin(listOf(KoinModuleConfig.applicationModule))

        val app = Javalin.create().apply {
            port(ApplicationConstants.DEFAULT_SERVER_PORT)
        }.event(JavalinEvent.SERVER_STOPPING){
            stopKoin()
        }.swagger(packageToScan = "com.github.gerivansantos").start()

        routeConfig.registerRoute(app)

        return app
    }
}

fun main (args: Array<String>){
   JavalinSwaggerApplication().init()
}