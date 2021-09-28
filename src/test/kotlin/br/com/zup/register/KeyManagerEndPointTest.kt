package br.com.zup.register

import br.com.zup.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false)
internal class KeyManagerEndPointTest(
   @Inject val grpcClient: PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub,
   @Inject val pixKeyRepository: PixKeyRepository
) {

    lateinit var response: PixKeyManagerResponse
    @BeforeEach
    fun setUp() {
        pixKeyRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        pixKeyRepository.deleteAll()
    }

    @Test
    fun `deve adicionar uma nova chave pix`(){
        response = grpcClient.register(
            PixKeyManagerRequest.newBuilder()
                .setId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .setKeyTipeRequest(KeyTypeRequest.CPF)
                .setValueKey("02467781054")
                .setAccountTypeRequest(AccountTypeRequest.CONTA_CORRENTE)
                .build()
        )
        with(response){
            assertNotNull(this.id)
            assertTrue(pixKeyRepository.existsById(this.id))
        }
    }

    @Test
    fun `deve retornar que ja existe uma chave registrata`(){

        val save = pixKeyRepository.save(
            PixKey(
                "02467781054",
                KeyType.CPF,
                AccountType.CONTA_CORRENTE,
                "c56dfef4-7901-44fb-84e2-a2cefb157890"
            )
        )

        val error = assertThrows<StatusRuntimeException> {
            val response = grpcClient.register(
                PixKeyManagerRequest.newBuilder()
                    .setId(save.clientId)
                    .setKeyTipeRequest(KeyTypeRequest.valueOf(save.keyType.name))
                    .setValueKey(save.pixKey)
                    .setAccountTypeRequest(AccountTypeRequest.valueOf(save.accountType.name))
                    .build()
            )
        }
        with(error){
            assertEquals(Status.ALREADY_EXISTS.code, this.status.code)
            assertEquals("Existing key", this.status.description)
        }


    }
    @Test
    fun `deve retornar invalid argument para entradas erradas de dados`(){
        val error = assertThrows<StatusRuntimeException> {
            val response = grpcClient.register(
                PixKeyManagerRequest.newBuilder()
                    .setId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                    .setKeyTipeRequest(KeyTypeRequest.CPF)
                    .setValueKey("0246")
                    .setAccountTypeRequest(AccountTypeRequest.CONTA_CORRENTE)
                    .build()
            )
        }
        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Invalid value in field: ${KeyTypeRequest.CPF.name}", status.description)
        }
    }


    @Test
    fun `deve retornar Not Existing client para clientes sem chave pix`(){
        val error = assertThrows<StatusRuntimeException> {
            val response = grpcClient.register(
                PixKeyManagerRequest.newBuilder()
                    .setId("c56dfef4-7901-44fb-84e2-a2cefb100000")
                    .setKeyTipeRequest(KeyTypeRequest.CPF)
                    .setValueKey("02467781054")
                    .setAccountTypeRequest(AccountTypeRequest.CONTA_CORRENTE)
                    .build()
            )
        }
        with(error){
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Not Existing client", status.description)
        }
    }

}

@Factory
class GrpcClient {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixKeyManagerServiceGrpc.PixKeyManagerServiceBlockingStub? {
        return PixKeyManagerServiceGrpc.newBlockingStub(channel)
    }
}