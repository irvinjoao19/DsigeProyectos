package com.dsige.dsigeproyectos.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dsige.dsigeproyectos.ui.fragments.engie.*

abstract class TabLayoutAdapter {

    class TabLayoutBaremoAdapter(
        fm: FragmentManager,
        private val numberOfTabs: Int,
        private val id: Int, private
        val tipo: Int
    ) : FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ParteDiarioGeneralFragment.newInstance(id)
                1 -> ParteDiarioBaremosFragment.newInstance(id)
                2 -> ParteDiarioMaterialFragment.newInstance(id)
                3 -> ParteDiarioFotoFragment.newInstance(id)
                4 -> ParteDiarioFirmFragment.newInstance(id)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

    class TabLayoutSolicitudAdapter(
        fm: FragmentManager,
        private val numberOfTabs: Int,
        val id: Int,
        val tipoMaterialSolicitud: Int,
        val tipoSolicitudId: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SolicitudGeneralFragment.newInstance(id, tipoMaterialSolicitud, tipoSolicitudId)
                1 -> SolicitudMaterialFragment.newInstance(id, tipoMaterialSolicitud, tipoSolicitudId)
                2 -> SolicitudPhotoFragment.newInstance(id, tipoMaterialSolicitud, tipoSolicitudId)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }
}