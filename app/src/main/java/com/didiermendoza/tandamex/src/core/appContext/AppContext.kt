package com.didiermendoza.tandamex.src.core.appContext
import android.content.Context
import com.didiermendoza.tandamex.src.core.di.AppContainer

object AppContainerHolder {
    private lateinit var container: AppContainer

    fun init(context: Context) {
        if (!::container.isInitialized) {
            container = AppContainer(context)
        }
    }

    fun get(): AppContainer {
        if (!::container.isInitialized) {
            throw IllegalStateException("AppContainerHolder no ha sido inicializado llama a init() primero")
        }
        return container
    }
}