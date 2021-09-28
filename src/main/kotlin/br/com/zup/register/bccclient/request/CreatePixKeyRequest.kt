package br.com.zup.register.bccclient.request

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreatePixKeyRequest(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest
) {


}