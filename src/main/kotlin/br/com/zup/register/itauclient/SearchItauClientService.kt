package br.com.zup.register.itauclient

import br.com.zup.ErrorDetails
import br.com.zup.PixKeyManagerRequest
import br.com.zup.PixKeyManagerResponse
import br.com.zup.register.AccountType
import br.com.zup.register.itauclient.response.ItauClientResponse
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory


@Singleton
class SearchItauClientService(@Inject val itauClient: ItauClient) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    /**
     * Busca dados do cliente na API do ITAU
     * return HttpResponse<ItauClientResponse>?
     */
    fun searchItauClientService(
        accountType: AccountType,
        idInterno: String,
        responseObserver: StreamObserver<PixKeyManagerResponse>?
    ): HttpResponse<ItauClientResponse>? {
        val accountType: String = accountType!!.name
        val clientId: String = idInterno
        var clientSearch: HttpResponse<ItauClientResponse>? = null

            clientSearch = itauClient.clientSearch(accountType, clientId)
            LOGGER.info(clientSearch.status.toString())
            if (clientSearch.status == HttpStatus.NOT_FOUND){
                LOGGER.error(clientSearch.status.toString() )
                val statusProto: Status = Status.newBuilder()
                    .setCode(Code.NOT_FOUND_VALUE)
                    .setMessage("Not Existing client")
                    .addDetails(
                        Any.pack(
                            ErrorDetails.newBuilder()
                                .setCode(404)
                                .setMessage("NOT EXISTS VALUE IN DATA BASE")
                                .build()
                        )
                    )
                    .build()
                responseObserver?.onError(StatusProto.toStatusRuntimeException(statusProto))
            }


        return clientSearch
    }
}