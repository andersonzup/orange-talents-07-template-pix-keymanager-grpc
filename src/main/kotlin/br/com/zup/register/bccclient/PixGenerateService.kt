package br.com.zup.register.bccclient

import br.com.zup.ErrorDetails
import br.com.zup.PixKeyManagerRequest
import br.com.zup.PixKeyManagerResponse
import br.com.zup.register.bccclient.request.CreatePixKeyRequest
import br.com.zup.register.bccclient.response.CreatePixKeyResponse
import br.com.zup.register.itauclient.response.ItauClientResponse
import br.com.zup.register.toBcbClientRequest
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PixGenerateService(@Inject val bccPixClient: BccPixClient) {
    /**
     * Serviço realiza solicitação de geração de chave pix para API do BCB.
     */
    fun pixGenerate(
        clientSearch: HttpResponse<ItauClientResponse>?,
        request: PixKeyManagerRequest?,
        responseObserver: StreamObserver<PixKeyManagerResponse>?
    ): HttpResponse<CreatePixKeyResponse>? {
        val itauClient: ItauClientResponse = clientSearch?.body!!.get()
        val requestBcb: CreatePixKeyRequest = itauClient.toBcbClientRequest(request!!)
        var creatPix: HttpResponse<CreatePixKeyResponse>? = null
        try {
            creatPix = bccPixClient.creatPix(requestBcb)
        } catch (e: HttpClientResponseException) {
            val statusProto: Status = Status.newBuilder()
                .setCode(Code.ALREADY_EXISTS_VALUE)
                .setMessage("Existing key")
                .addDetails(
                    Any.pack(
                        ErrorDetails.newBuilder()
                            .setCode(422)
                            .setMessage("EXISTS VALUE IN DATA BASE")
                            .build()
                    )
                )
                .build()
            responseObserver?.onError(StatusProto.toStatusRuntimeException(statusProto))
        }
        return creatPix
    }
}