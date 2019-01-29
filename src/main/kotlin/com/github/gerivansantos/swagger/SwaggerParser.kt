package com.github.gerivansantos.swagger

import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.OpenAPI
import org.reflections.Reflections
import javax.ws.rs.Path

object SwaggerParser{

    fun scan(packageName: String): OpenAPI{
        return getOpenAPI(packageName)
    }

    private fun getOpenAPI(packageName: String): OpenAPI {
        val reflections = Reflections(packageName)
        val apiClasses = reflections.getTypesAnnotatedWith(Path::class.java)
        var openAPI = OpenAPI()
        if(apiClasses.count() > 0)
        {
            apiClasses.add(reflections.getTypesAnnotatedWith(OpenAPIDefinition::class.java).first())
            val swaggerConfiguration =
                SwaggerConfiguration().resourcePackages(mutableSetOf(packageName)).readAllResources(true)
            val reader = Reader(swaggerConfiguration)
            openAPI = reader.read(apiClasses)
        }
        return  openAPI
    }
}