package com.wallet.pennyservice

import com.wallet.pennyservice.dto.LoginRequest
import com.wallet.pennyservice.dto.WalletRequest
import com.wallet.pennyservice.entity.Player
import com.wallet.pennyservice.repository.PlayerRepository
import com.wallet.pennyservice.repository.TransactionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    private lateinit var token: String

    @BeforeEach
    fun setup() {

        transactionRepository.deleteAll()
        playerRepository.deleteAll()

        playerRepository.save(
            Player(
                pid = "player1",
                name = "Test Player",
                balance = BigDecimal("10.0")
            )
        )

        val loginRequest = LoginRequest(
            clientId = "VeikGameEngine",
            secret = "blackjack"
        )

        val result = mockMvc.post("/api/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }.andReturn()

        val response = result.response.contentAsString

        token = objectMapper.readTree(response)
            .get("token")
            .asString()
    }

    @Test
    fun `Test1 - purchase game for player`() {

        val request = WalletRequest(
            uid = "test1",
            pid = "player1",
            sum = BigDecimal("2.0")
        )

        mockMvc.post("/api/purchase") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(8.0) }
            }
    }

    @Test
    fun `Test2 - reject purchase with error insufficient funds`() {

        val request = WalletRequest(
            uid = "test2",
            pid = "player1",
            sum = BigDecimal("100.0")
        )

        mockMvc.post("/api/purchase") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isPaymentRequired() }
                jsonPath("$.message") {
                    value("Not enough money")
                }
            }
    }

    @Test
    fun `Test3 - check idempotency, transaction exists and purchase happens only once`() {

        val request = WalletRequest(
            uid = "test3",
            pid = "player1",
            sum = BigDecimal("2.0")
        )

        repeat(2) {
            mockMvc.post("/api/purchase") {
                header("Authorization", "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.balance") { value(8.0) }
                }
        }
    }

    @Test
    fun `Test4 - reject unauthorized request, without Authtoken`() {

        val request = WalletRequest(
            uid = "test4",
            pid = "player1",
            sum = BigDecimal("2.0")
        )
        mockMvc.post("/api/purchase") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @Test
    fun `Test5 - player win, add player balnce`() {
        val request = WalletRequest(
            uid = "test5",
            pid = "player1",
            sum = BigDecimal("2.0")
        )

        mockMvc.post("/api/win") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.balance") { value(12.0) }
            }
    }

    @Test
    fun `Test6 - player win idempotency, add player balance only once`() {
        val request = WalletRequest(
            uid = "test6",
            pid = "player1",
            sum = BigDecimal("2.0")
        )

        repeat(2) {
            mockMvc.post("/api/win") {
                header("Authorization", "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.balance") { value(12.0) }
                }
        }
    }

    @Test
    fun `Test7 - reject invalid amount`() {
        val request = WalletRequest(
            uid = "test7",
            pid = "player1",
            sum = BigDecimal("-2.0")
        )
        mockMvc.post("/api/purchase") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `Test8 - player does not exist`() {
        val request = WalletRequest(
            uid = "test8",
            pid = "unknown",
            sum = BigDecimal("2.0")
        )
        mockMvc.post("/api/purchase") {
            header("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isNotFound() }
            }
    }

}
