package br.com.zup.register

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class KeyManagerRequest(
    @field:NotBlank
    val id: String?,
    @field:NotBlank
    val keyType: String?,
    @field:NotBlank
    @field:Size(max = 77)
    val valueKey: String?,
    @field:NotBlank
    val accountType: String?
) {
}