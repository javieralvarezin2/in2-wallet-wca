package es.in2.wallet.waltid.services

import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWK
import es.in2.wallet.SERVICE_MATRIX
import id.walt.crypto.KeyAlgorithm
import id.walt.crypto.KeyId
import id.walt.servicematrix.ServiceMatrix
import id.walt.services.key.KeyFormat
import id.walt.services.key.KeyService
import id.walt.services.keystore.KeyType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service

interface CustomKeyService {
    fun generateKey(): KeyId
    fun getECKeyFromKid(kid: String): ECKey
}

@Service
class CustomKeyServiceImpl : CustomKeyService {

    private val log: Logger = LogManager.getLogger(CustomKeyService::class.java)

    override fun generateKey(): KeyId {
        log.info("Key Service - Generate Key")
        ServiceMatrix(SERVICE_MATRIX)
        return KeyService.getService().generate(KeyAlgorithm.ECDSA_Secp256r1)
    }

    override fun getECKeyFromKid(kid: String): ECKey {
        log.info("Key Service - Get ECKey by Kid")
        ServiceMatrix(SERVICE_MATRIX)
        val jwk = KeyService.getService().export(kid, KeyFormat.JWK, KeyType.PRIVATE)
        return JWK.parse(jwk).toECKey()
    }

}