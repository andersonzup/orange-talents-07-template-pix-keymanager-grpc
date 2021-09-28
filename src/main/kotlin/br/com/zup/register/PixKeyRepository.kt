package br.com.zup.register

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixKeyRepository : JpaRepository<PixKey, Long> {
    fun existsByPixKey(valueKey: String?): Boolean
    fun deleteByPixKey(pixKeyId: String?)
    fun existsByPixKeyAndClientId(pixKeyId: String?, clienteId: String?): Boolean

}