package br.com.zup.register

import br.com.zup.ErrorDetails
import br.com.zup.PixKeyManagerResponse
import br.com.zup.register.itauclient.SearchItauClientService
import br.com.zup.validator.requestValidation
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
class RegisterService(
    @Inject val pixKeyRepository: PixKeyRepository,
    @Inject val itauClientService: SearchItauClientService
) {

    @Transactional
    fun register(@Valid newKey: KeyManagerRequest, responseObserver: StreamObserver<PixKeyManagerResponse>?): PixKey {
        requestValidation(newKey, responseObserver)

        if (pixKeyRepository.existsByPixKey(newKey.valueKey)) {
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
            responseObserver?.onCompleted()
        }
        val response = itauClientService.searchItauClientService(AccountType.valueOf(newKey.accountType!!), newKey.id!!, responseObserver)

        return pixKeyRepository.save(newKey!!.toModel())
    }




}


