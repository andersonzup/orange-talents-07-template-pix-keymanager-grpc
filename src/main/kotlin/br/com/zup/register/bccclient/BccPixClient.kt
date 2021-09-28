package br.com.zup.register.bccclient

import br.com.zup.register.bccclient.request.CreatePixKeyRequest
import br.com.zup.register.bccclient.response.CreatePixKeyResponse
import br.com.zup.register.bccclient.response.PixKeyDetailsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client


@Client(value = "http://localhost:8082/api/v1/pix/keys/")
interface BccPixClient {

    @Get(value = "{keyPix}")
    @Consumes(MediaType.APPLICATION_XML)
    fun pixSearch(@PathVariable keyPix: String) : HttpResponse<PixKeyDetailsResponse>

    @Post
    @Produces(MediaType.APPLICATION_XML)
    fun creatPix(@Body request: CreatePixKeyRequest) : HttpResponse<CreatePixKeyResponse>
}