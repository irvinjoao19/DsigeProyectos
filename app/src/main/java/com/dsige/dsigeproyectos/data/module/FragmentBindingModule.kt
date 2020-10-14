package com.dsige.dsigeproyectos.data.module

import com.dsige.dsigeproyectos.ui.fragments.*
import com.dsige.dsigeproyectos.ui.fragments.engie.*
import com.dsige.dsigeproyectos.ui.fragments.logistica.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

abstract class FragmentBindingModule {

    @Module
    abstract class Main {
        @ContributesAndroidInjector
        internal abstract fun providMainFragment(): MainFragment
        //@ContributesAndroidInjector
        //internal abstract fun providSendFragment(): SendFragment
    }

    @Module
    abstract class Camera {
        @ContributesAndroidInjector
        internal abstract fun providCameraFragment(): CameraFragment
    }

    @Module
    abstract class ParteDiario {

        @ContributesAndroidInjector
        internal abstract fun providParteDiarioGeneralFragment(): ParteDiarioGeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providParteDiarioBaremosFragment(): ParteDiarioBaremosFragment

        @ContributesAndroidInjector
        internal abstract fun providParteDiarioMaterialFragment(): ParteDiarioMaterialFragment

        @ContributesAndroidInjector
        internal abstract fun providParteDiarioFotoFragment(): ParteDiarioFotoFragment

        @ContributesAndroidInjector
        internal abstract fun providParteDiarioFirmFragment(): ParteDiarioFirmFragment
    }

    @Module
    abstract class FormSolicitud {

        @ContributesAndroidInjector
        internal abstract fun providSolicitudGeneralFragment(): SolicitudGeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providSolicitudMaterialFragment(): SolicitudMaterialFragment

        @ContributesAndroidInjector
        internal abstract fun providSolicitudPhotoFragment(): SolicitudPhotoFragment
    }

    // LOGISTICA

    @Module
    abstract class Requerimiento {

        @ContributesAndroidInjector
        internal abstract fun providGeneralFragment(): GeneralFragment

        @ContributesAndroidInjector
        internal abstract fun providMaterialFragment(): MaterialFragment

    }
}