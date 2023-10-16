package es.in2.wallet.controller


import com.fasterxml.jackson.databind.ObjectMapper
import es.in2.wallet.api.model.dto.QrContentDTO
import es.in2.wallet.wca.controller.VerifiableCredentialController
import es.in2.wallet.wca.model.dto.CredentialRequestDTO
import es.in2.wallet.wca.service.VerifiableCredentialService
import es.in2.wallet.wca.service.impl.VerifiableCredentialServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class VerifiableCredentialControllerTest {

    @Mock
    private lateinit var verifiableCredentialService: VerifiableCredentialService

    @InjectMocks
    private lateinit var verifiableCredentialController: VerifiableCredentialController

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(SiopControllerTest::class.java)
        mockMvc = MockMvcBuilders.standaloneSetup(verifiableCredentialController).build()
    }

    @Test
    fun `getVC should return 200 OK`() {
        val credentialRequestDTO = CredentialRequestDTO(issuerName = "issuerName", did = "key")

        Mockito.doNothing().`when`(verifiableCredentialService).getVerifiableCredential(credentialRequestDTO)

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapper().writeValueAsString(credentialRequestDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated)

    }

    //TODO - Arreglar test. Lanza error 400
//    @Test
//    fun `getCredentialIssuerMetadata should return 201 OK`() {
//        val qrContentDTO = QrContentDTO(content = "http://issuer-api:8081/credential-offer?credential_offer_uri=http://issuer-api:8081/credential-offer/k-un2ZiMSiKEpXQXqKScPg")
//
//        Mockito.doNothing().`when`(verifiableCredentialService).getCredentialIssuerMetadata( "{\"qr_content\":\"${qrContentDTO.content}\"}")
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/api/credentials/issuer-metadata")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(ObjectMapper().writeValueAsString(qrContentDTO))
//        )
//                .andExpect(MockMvcResultMatchers.status().isCreated)
//
//    }

}