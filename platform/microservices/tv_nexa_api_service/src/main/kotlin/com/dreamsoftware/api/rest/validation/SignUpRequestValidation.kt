package com.dreamsoftware.api.rest.validation

import com.dreamsoftware.api.rest.dto.request.SignUpRequestDTO
import com.dreamsoftware.api.rest.utils.isPasswordValid
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.configureSignUpRequestValidation() {
    validateSignUpRequestDTO()
}

private fun RequestValidationConfig.validateSignUpRequestDTO() {
    validate<SignUpRequestDTO> { request ->
        request.validateDestination()
    }
}

private fun SignUpRequestDTO.validateDestination() = when {
    username.isBlank() ->  ValidationResult.Invalid("username can not be empty, You must specify it")
    password.isBlank() ->  ValidationResult.Invalid("password can not be empty, You must specify it")
    !password.isPasswordValid() ->  ValidationResult.Invalid("You must provide a valid password, min 8 characters with a special character")
    email.isBlank() ->  ValidationResult.Invalid("email can not be empty, You must specify it")
    !email.matches(Regex("^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$")) -> ValidationResult.Invalid("email must be a valid email address")
    firstName.isBlank() -> ValidationResult.Invalid("firstName can not be empty, You must specify it")
    lastName.isBlank() -> ValidationResult.Invalid("lastName can not be empty, You must specify it")
    else ->  ValidationResult.Valid
}