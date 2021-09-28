package br.com.zup.remove

import br.com.zup.*
import br.com.zup.register.AccountType
import br.com.zup.register.KeyType
import br.com.zup.register.PixKey
import br.com.zup.register.PixKeyRepository
import br.com.zup.validator.requestValidation
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


@MicronautTest(transactional = false)
internal class KeyManagerRemoveEndpointTest(
    @Inject val grpcRemoveClient: PixKeyManagerRemoveServiceGrpc.PixKeyManagerRemoveServiceBlockingStub,
    @Inject val pixKeyRepository: PixKeyRepository
) {

    @BeforeEach
    fun setUp() {
        pixKeyRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        pixKeyRepository.deleteAll()
    }

    @Test
    fun `Deve Remover Uma Chave No Banco De Dados`(){
        val pixKey = pixKeyRepository.save(PixKey("02467781054", KeyType.CPF, AccountType.CONTA_CORRENTE,"c56dfef4-7901-44fb-84e2-a2cefb157890"))
        val request = RemoveKeyRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setPixKeyId(pixKey.pixKey)
            .build()
        val removeKeyResponse: RemoveKeyResponse = grpcRemoveClient.remove(request)

        assertEquals("Removed Key with id: ${request.pixKeyId}" ,removeKeyResponse.pixKeyId)

    }
    @Test
    fun `Deve Retornar Not Found`(){
        val pixKey = pixKeyRepository.save(PixKey("02467781054", KeyType.CPF, AccountType.CONTA_CORRENTE, "c56dfef4-7901-44fb-84e2-a2cefb157890"))
        val request = RemoveKeyRequest.newBuilder()
            .setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setPixKeyId("02467781000")
            .build()
        val error = org.junit.jupiter.api.assertThrows<StatusRuntimeException> {
            val removeKeyResponse: RemoveKeyResponse = grpcRemoveClient.remove(request)
        }

        with(error){
            assertEquals(Status.NOT_FOUND.code, this.status.code)
            assertEquals("Not Existing key" , this.status.description)
        }
    }

}

@Factory
class GrpcRemoveClient {
    @Singleton
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixKeyManagerRemoveServiceGrpc.PixKeyManagerRemoveServiceBlockingStub? {
        return PixKeyManagerRemoveServiceGrpc.newBlockingStub(channel)
    }
}