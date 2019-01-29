package com.github.gerivansantos.config.koin

import com.github.gerivansantos.config.route.RouteConfig
import com.github.gerivansantos.controller.UserController
import org.koin.dsl.module.module

object KoinModuleConfig{
    val applicationModule = module {
        single<UserController> { UserController()}
        single<RouteConfig> { RouteConfig(get()) }

    }
}