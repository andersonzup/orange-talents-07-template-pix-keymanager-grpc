package br.com.zup.register.bccclient.request

import io.micronaut.core.annotation.Introspected

@Introspected
data class BankAccountRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,//Tipo de conta (CACC=Conta Corrente; SVGS=Conta Poupança)
    val accountType: String
) {

}
