package br.com.zup.register

import br.com.zup.*
import br.com.zup.register.bccclient.PixGenerateService
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory



@Singleton
class KeyManagerEndPoint(
    @Inject val registerService: RegisterService,
    @Inject val pixGenerateService: PixGenerateService
    ) :
    PixKeyManagerServiceGrpc.PixKeyManagerServiceImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun register(request: PixKeyManagerRequest?,
                          responseObserver: StreamObserver<PixKeyManagerResponse>?) {

        LOGGER.info("Receiving request in the registration method")

        val newKey = KeyManagerRequest(
            request?.id, request?.keyTipeRequest?.name, request?.valueKey, request?.accountTypeRequest?.name)

        val pixKey = registerService.register(newKey, responseObserver)

        //val creatPix: HttpResponse<CreatePixKeyResponse>? = pixGenerateService.pixGenerate(clientSearch, request, responseObserver)


        val response = pixKey.id?.let {
            PixKeyManagerResponse.newBuilder()
                .setId(it).build()
        }

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }




}