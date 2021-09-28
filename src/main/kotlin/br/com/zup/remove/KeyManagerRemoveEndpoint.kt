package br.com.zup.remove


import br.com.zup.PixKeyManagerRemoveServiceGrpc
import br.com.zup.RemoveKeyRequest
import br.com.zup.RemoveKeyResponse
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class KeyManagerRemoveEndpoint(
    val removeService: KeyManagerRemoveService
) : PixKeyManagerRemoveServiceGrpc.PixKeyManagerRemoveServiceImplBase(){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun remove(request: RemoveKeyRequest, responseObserver: StreamObserver<RemoveKeyResponse>?) {

        val removeRequest = RemoveRequest(request.clienteId, request.pixKeyId!!)
        LOGGER.info("Access remove EndPoint ${removeRequest.pixKeyId}")
        removeService.remove(removeRequest, responseObserver)

        responseObserver?.onNext(RemoveKeyResponse.newBuilder().setPixKeyId("Removed Key with id: ${removeRequest.pixKeyId}")
            .setClienteId(removeRequest.clienteId).build())
        responseObserver?.onCompleted()

    }


}