package br.com.zup.register.itauclient.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class ItauClientResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse
) {

}
