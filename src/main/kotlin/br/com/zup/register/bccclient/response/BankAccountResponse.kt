package br.com.zup.register.bccclient.response

import io.micronaut.core.annotation.Introspected

@Introspected
data class BankAccountResponse(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: String
) {

}
