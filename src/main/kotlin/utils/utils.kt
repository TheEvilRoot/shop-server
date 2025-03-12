package lol.malinovskaya.utils

import lol.malinovskaya.model.api.ErrorResponse

fun Throwable.response(code: String? = null): ErrorResponse {
    return ErrorResponse(
        code = code ?: "GENERIC_ERROR",
        message = localizedMessage ?: ""
    )
}