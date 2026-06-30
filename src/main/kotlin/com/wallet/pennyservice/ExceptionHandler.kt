package com.wallet.pennyservice
import com.wallet.pennyservice.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.bind.MethodArgumentNotValidException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(
                status = "error",
                message = e.reason ?: "Unexpected error"
            ),
            e.statusCode as HttpStatus
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage
            ?: "Validation error"

        return ResponseEntity(ErrorResponse(
                status = "error",
                message = message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

}
