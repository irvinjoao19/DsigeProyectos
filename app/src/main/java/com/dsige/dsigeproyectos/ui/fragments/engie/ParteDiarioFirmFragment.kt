package com.dsige.dsigeproyectos.ui.fragments.engie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.activities.FirmActivity
import com.dsige.dsigeproyectos.ui.adapters.FirmAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_parte_diario_firm.*
import kotlinx.android.synthetic.main.fragment_parte_diario_firm.recyclerView
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class ParteDiarioFirmFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (p.obraTd.isNotEmpty()) {
            startActivity(Intent(context, FirmActivity::class.java).putExtra("parteDiarioId", parteDiarioId))
        } else {
            parteDiarioViewModel.setError("Completar primer formulario")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel

    var parteDiarioId: Int = 0
    lateinit var p: ParteDiario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = ParteDiario()
        arguments?.let {
            parteDiarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parte_diario_firm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parteDiarioViewModel = ViewModelProvider(this, viewModelFactory).get(
            ParteDiarioViewModel::class.java)
        fabFirma.setOnClickListener(this)
        bindUI()
    }

    private fun bindUI() {
        val layoutManager = LinearLayoutManager(context)
        val firmAdapter = FirmAdapter()
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = firmAdapter

        parteDiarioViewModel.getParteDiarioById(parteDiarioId).observe(viewLifecycleOwner, Observer { d ->
            if (d != null) {
                p = d
                if (d.firmaMovil.isNotEmpty()) {
                    val firm = ArrayList<MenuPrincipal>()
                    firm.add(MenuPrincipal(1, d.firmaMovil, 1))
                    firmAdapter.addItems(firm)
                }

                if (d.estadoCodigo == "121") {
                    fabFirma.visibility = View.GONE
                }
            }
        })

        parteDiarioViewModel.error.observe(viewLifecycleOwner, Observer { m ->
            Util.snackBarMensaje(view!!, m)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(parteDiarioId: Int) =
            ParteDiarioFirmFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, parteDiarioId)
                }
            }
    }
}