package com.hyeonwoo.compose_cocktail_recipes.util

sealed class RequestStatus {
    object Loading : RequestStatus()
    object Success : RequestStatus()
    object Error : RequestStatus()
}