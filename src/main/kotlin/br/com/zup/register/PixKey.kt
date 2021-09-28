package br.com.zup.register


import javax.persistence.*


@Entity
class PixKey(
    @Column(nullable = false) val pixKey : String,
    @Column(nullable = false) val keyType: KeyType,
    @Column(nullable = false) val accountType: AccountType,
    @Column(nullable = false) val clientId: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}