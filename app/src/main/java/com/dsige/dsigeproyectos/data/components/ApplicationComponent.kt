package com.dsige.dsigeproyectos.data.components

import android.app.Application
import com.dsige.dsigeproyectos.data.module.*
import com.dsige.dsigeproyectos.data.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        RetrofitModule::class,
        DataBaseModule::class,
        ServicesModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}