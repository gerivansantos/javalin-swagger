package com.github.gerivansantos.swagger

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.javalin.Context
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.core.util.OptionalDependency
import io.javalin.core.util.Util
import io.swagger.v3.oas.models.OpenAPI
import org.apache.http.entity.ContentType

class SuperSwaggerRenderer(private val specUrl: String) : Handler {

    private val swaggerVersion = OptionalDependency.SWAGGERUI.version

    override fun handle(ctx: Context) {

        if (ctx.queryParam("spec") != null)
            ctx.result(Util.getResource(ctx.queryParam("spec")!!)!!.readText())
        else ctx.html(
            """
            <head>
                <meta charset="UTF-8">
                <title>Swagger UI</title>
                <link rel="icon" type="image/png" href="/webjars/swagger-ui/$swaggerVersion/favicon-16x16.png" sizes="16x16" />
                <link rel="stylesheet" href="/webjars/swagger-ui/$swaggerVersion/swagger-ui.css" >
                <script src="/webjars/swagger-ui/$swaggerVersion/swagger-ui-bundle.js"></script>
                <style>body{background:#fafafa;}</style>
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script>
                    window.ui = SwaggerUIBundle({
                        url: "$specUrl",
                        dom_id: "#swagger-ui",
                        deepLinking: true,
                        presets: [SwaggerUIBundle.presets.apis],
                    });
                </script>
            </body>""".trimIndent()
        )
    }

}

fun openApiToJson(openAPI: OpenAPI) : String{
    val objectMapper = ObjectMapper()
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    return objectMapper.writeValueAsString(openAPI)
}

fun Javalin.swagger(
    enable: Boolean = true,
    enableUiEndpoint: Boolean = true,
    enableJsonEndpoint: Boolean = true,
    packageToScan: String = "*",
    apiFileName: String = "openapi.json",
    swaggerUIendPoint: String = "swagger-ui"): Javalin{

    if(enable)
    {
        this.enableWebJars()

        val openApi = SwaggerParser.scan(packageToScan)

        //Monta rotas do arquivo Json do Swagger e do Swagger UI
        this.routes{

            if(enableJsonEndpoint){
                get(apiFileName){
                    it.result(openApiToJson(openApi)).contentType(ContentType.APPLICATION_JSON.mimeType)
                }
            }

            if(enableUiEndpoint){
                get(swaggerUIendPoint, SuperSwaggerRenderer("/$apiFileName"))
            }
        }


    }
    return this
}