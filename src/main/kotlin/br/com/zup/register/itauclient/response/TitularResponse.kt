package br.com.zup.register.itauclient.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularResponse(
    val id :String,
    val nome: String,
    val cpf: String
) {

}
