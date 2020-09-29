package com.dsige.dsigeproyectos.data.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsige.dsigeproyectos.data.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsuarioViewModel::class)
    internal abstract fun bindUserViewModel(usuarioViewModel: UsuarioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistroViewModel::class)
    internal abstract fun bindSuministroViewModel(registroViewModel: RegistroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VehiculoViewModel::class)
    internal abstract fun bindVehiculoViewModel(vehiculoViewModel: VehiculoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ParteDiarioViewModel::class)
    internal abstract fun bindParteDiarioViewModel(parteDiarioViewModel: ParteDiarioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SolicitudViewModel::class)
    internal abstract fun bindSolicitudViewModel(solicitudViewModel: SolicitudViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MaterialViewModel::class)
    internal abstract fun bindMaterialViewModel(materialViewModel: MaterialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LogisticaViewModel::class)
    internal abstract fun bindLogisticaViewModel(logisticaViewModel: LogisticaViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}