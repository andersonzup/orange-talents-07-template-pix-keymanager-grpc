package br.com.zup.validator

import br.com.zup.ErrorDetails
import br.com.zup.PixKeyManagerRequest
import br.com.zup.PixKeyManagerResponse
import br.com.zup.register.KeyManagerRequest
import br.com.zup.register.KeyType
import br.com.zup.register.PixKeyRepository
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver


fun pixKeyValidator(
    regex: String, keyType: KeyType, request: KeyManagerRequest?,
    responseObserver: StreamObserver<PixKeyManagerResponse>?
) {
        if (request?.keyType.toString().equals(keyType.toString()) &&
            !request?.valueKey!!.matches(regex.toRegex())
        ) {
            val statusProto: Status = Status.newBuilder()
                .setCode(Code.INVALID_ARGUMENT_VALUE)
                .setMessage("Invalid value in field: ${keyType}")
                .addDetails(
                    Any.pack(
                        ErrorDetails.newBuilder()
                            .setCode(400)
                            .setMessage("Invalid value in field: ${keyType}")
                            .build()
                    )
                )
                .build()
            responseObserver?.onError(StatusProto.toStatusRuntimeException(statusProto))

        }

}

fun requestValidation(
    request: KeyManagerRequest?,
    responseObserver: StreamObserver<PixKeyManagerResponse>?
) {
    with(request) {
        pixKeyValidator("^[0-9]{11}\$", KeyType.valueOf("CPF"), this, responseObserver)
        pixKeyValidator("^\\+[1-9][0-9]\\d{1,14}\$", KeyType.valueOf("CELL_NUMBER"), this, responseObserver)
        pixKeyValidator(
            "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)\$",
            KeyType.valueOf("EMAIL"),
            this,
            responseObserver
        )
    }
}
