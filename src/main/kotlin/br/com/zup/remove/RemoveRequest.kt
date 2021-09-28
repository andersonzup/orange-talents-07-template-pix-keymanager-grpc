package br.com.zup.remove

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class RemoveRequest(
    @field:NotBlank val clienteId: String?,
    @field:NotNull val pixKeyId: String?
    ) {
}