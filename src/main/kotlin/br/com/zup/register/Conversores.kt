package br.com.zup.register

import br.com.zup.PixKeyManagerRequest
import br.com.zup.register.bccclient.request.AccountTypeRequestBCB
import br.com.zup.register.bccclient.request.BankAccountRequest
import br.com.zup.register.bccclient.request.CreatePixKeyRequest
import br.com.zup.register.bccclient.request.OwnerRequest
import br.com.zup.register.itauclient.response.ItauClientResponse
import java.util.*

/**
 * File responsible for the converter methods used in KeyManagerEndPoint
 */
fun ItauClientResponse.toBcbClientRequest(request: PixKeyManagerRequest): CreatePixKeyRequest {
    return CreatePixKeyRequest(
        request.keyTipeRequest.name,
        request.generatedKey(),
        BankAccountRequest(
            instituicao.ispb, agencia, numero, convertAccauntTypeRequest(request.accountTypeRequest.name)
        ),
        OwnerRequest(
            "NATURAL_PERSON", titular.nome, titular.cpf
        )
    )
}
fun convertAccauntTypeRequest(value: String): String {
    if (value == AccountType.CONTA_CORRENTE.name) {
        return AccountTypeRequestBCB.CACC.name
    } else {
        return AccountTypeRequestBCB.SVGS.name
    }
}

fun PixKeyManagerRequest.generatedKey(): String {
    if (keyTipeRequest.name == "RANDOM_KEY") {
        return UUID.randomUUID().toString()
    }
    return valueKey
}

fun KeyManagerRequest.toModel(): PixKey{
    return PixKey(valueKey!!, KeyType.valueOf(keyType!!), AccountType.valueOf(accountType!!), id!!)
}

fun convertAccauntTypeModel(value: String): AccountType{
    if (value == AccountTypeRequestBCB.CACC.name){
        AccountType.CONTA_CORRENTE
    }
    return AccountType.CONTA_POUPANCA
}
