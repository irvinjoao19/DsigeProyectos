package com.dsige.dsigeproyectos.data.module

import com.dsige.dsigeproyectos.ui.activities.*
import com.dsige.dsigeproyectos.ui.activities.engie.*
import com.dsige.dsigeproyectos.ui.activities.logistica.*
import com.dsige.dsigeproyectos.ui.activities.trinidad.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindMenuActivity(): MenuActivity

    @ContributesAndroidInjector
    internal abstract fun bindSubMainActivity(): SubMainActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Main::class])
    internal abstract fun bindPrincipalActivity(): PrincipalActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Camera::class])
    internal abstract fun bindCameraActivity(): CameraActivity

    @ContributesAndroidInjector
    internal abstract fun bindPreviewCameraActivity(): PreviewCameraActivity

    @ContributesAndroidInjector
    internal abstract fun bindRegistroActivity(): RegistroActivity

    @ContributesAndroidInjector
    internal abstract fun bindDetailActivity(): DetailActivity

    // TODO ENGIE

    @ContributesAndroidInjector
    internal abstract fun bindParteDiarioActivity(): ParteDiarioActivity

    @ContributesAndroidInjector
    internal abstract fun bindFiltroActivity(): FiltroActivity

    @ContributesAndroidInjector
    internal abstract fun bindFirmaActivity(): FirmActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.ParteDiario::class])
    internal abstract fun bindParteDiarioFormActivity(): ParteDiarioFormActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.FormSolicitud::class])
    internal abstract fun bindSolicitudMaterialFormActivity(): SolicitudMaterialFormActivity

    @ContributesAndroidInjector
    internal abstract fun bindRequerimientoSolicitudActivity(): RequerimientoSolicitudActivity

    // TODO LOGISTICA

    @ContributesAndroidInjector
    internal abstract fun bindAprobacionActivity(): AprobacionActivity

    @ContributesAndroidInjector
    internal abstract fun bindAprobationDetailActivity(): AprobationDetailActivity

    @ContributesAndroidInjector
    internal abstract fun bindRequerimientoActivity(): RequerimientoActivity

    @ContributesAndroidInjector(modules = [FragmentBindingModule.Requerimiento::class])
    internal abstract fun bindRequerimientoFormActivity(): RequerimientoFormActivity
}