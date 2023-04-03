package es.in2.wallet.services

import com.nimbusds.jwt.SignedJWT
import es.in2.wallet.waltid.services.CustomDidService
import id.walt.custodian.Custodian
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service
import java.time.Instant

interface SiopVerifiablePresentationService {
    fun createVerifiablePresentation(verifiableCredentials: List<String>, format: String): String
}

@Service
class SiopVerifiablePresentationServiceImpl(
    private val customDidService: CustomDidService
) : SiopVerifiablePresentationService {

    private val log: Logger = LogManager.getLogger(SiopVerifiablePresentationServiceImpl::class.java)

    override fun createVerifiablePresentation(verifiableCredentials: List<String>, format: String): String {

        val verifiableCredential = verifiableCredentials[0]
        log.info("VerifiableCredential: $verifiableCredential")

        val parsedVerifiableCredential = SignedJWT.parse(verifiableCredential)

        val payloadToJson = parsedVerifiableCredential.payload.toJSONObject()
        val subjectDid = payloadToJson["sub"]
        log.info("Subject Did: $subjectDid")

        /*
            The holder DID MUST be received by the Wallet implementation, and it MUST match with the
            subject_id of, at least, one of the VCs attached.
            That VP MUST be signed using the PrivateKey related with the holderDID.
         */
        val holderDid = customDidService.generateDidKey()

        /*
            The holder SHOULD be able to modify the attribute 'expiration_date' by any of its
            Verifiable Presentation.
        */
        val secondsToAdd: Long = 600

        return Custodian.getService().createPresentation(
            vcs = verifiableCredentials,
            holderDid = holderDid,
            expirationDate = Instant.now().plusSeconds(secondsToAdd)
        )
    }

}