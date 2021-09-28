package br.com.zup.remove

import br.com.zup.ErrorDetails
import br.com.zup.RemoveKeyResponse
import br.com.zup.register.PixKeyRepository
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class KeyManagerRemoveService(
    val pixKeyRepository: PixKeyRepository
    ) {
    
    @Transactional
    fun remove(@Valid request: RemoveRequest?, responseObserver: StreamObserver<RemoveKeyResponse>?){
        existsPixKey(request!!.pixKeyId!!, responseObserver)
        pixKeyRepository.deleteByPixKey(request.pixKeyId)
    }



    private fun existsPixKey(
        pixId: String,
        responseObserver: StreamObserver<RemoveKeyResponse>?
    ) {
        if (!pixKeyRepository.existsByPixKey(pixId)) {
            val statusProto: Status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage("Not Existing key")
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
            responseObserver?.onCompleted()
        }
    }


}