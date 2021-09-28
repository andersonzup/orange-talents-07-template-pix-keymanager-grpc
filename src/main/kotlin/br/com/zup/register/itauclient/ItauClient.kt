package br.com.zup.register.itauclient

import br.com.zup.register.itauclient.response.ItauClientResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(value = "http://localhost:9091/api/v1/clientes/")
interface ItauClient {

    @Get("{clientId}/contas")
    fun clientSearch(@QueryValue tipo: String , @PathVariable clientId: String) : HttpResponse<ItauClientResponse>

}