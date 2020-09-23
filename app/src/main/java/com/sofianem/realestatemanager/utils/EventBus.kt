package com.sofianem.realestatemanager.utils

interface EventBus {

    fun post(event: kotlin.Any)
    fun register(listener: kotlin.Any)
    fun unregister(listener: kotlin.Any)
}