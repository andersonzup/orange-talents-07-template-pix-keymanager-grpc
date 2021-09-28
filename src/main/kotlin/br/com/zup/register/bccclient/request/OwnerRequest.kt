package br.com.zup.register.bccclient.request

data class OwnerRequest(
    val type: String,
    val name: String,
    val taxIdNumber: String
    ) {

}
