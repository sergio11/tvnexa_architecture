package com.dreamsoftware.api.data.cache.exception

sealed class CacheException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class ItemNotFoundException(message: String? = null, cause: Throwable? = null) : CacheException(message, cause)
    class InternalErrorException(message: String? = null, cause: Throwable? = null) : CacheException(message, cause)
}