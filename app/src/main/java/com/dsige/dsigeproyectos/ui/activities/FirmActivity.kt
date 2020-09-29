package com.dsige.dsigeproyectos.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario
import com.dsige.dsigeproyectos.data.viewModel.ParteDiarioViewModel
import com.dsige.dsigeproyectos.data.viewModel.ViewModelFactory
import com.dsige.dsigeproyectos.helper.Util
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_firm.*
import javax.inject.Inject

class FirmActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabFirma -> {
                if (paintView.validDraw()) {
                    val name = paintView.save(this, parteDiarioId)
                    p.firmaMovil = name
                    parteDiarioViewModel.validateParteDiario(p)
                } else {
                    Util.toastMensaje(this, "Debes de Firmar")
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var parteDiarioViewModel: ParteDiarioViewModel

    lateinit var p: ParteDiario
    var parteDiarioId: Int = 0

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.camera).setVisible(false).isEnabled = false
        menu.findItem(R.id.ok).setVisible(false).isEnabled = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.eraser -> {
                paintView.clear()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firm)
        p = ParteDiario()
        val b = intent.extras
        if (b != null) {
            parteDiarioId = b.getInt("parteDiarioId")
            bindUI()
            menssage()
            success()
        }
    }

    private fun bindUI() {
        parteDiarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(ParteDiarioViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Firma"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        paintView.init(metrics)
        fabFirma.setOnClickListener(this)
        parteDiarioViewModel.getParteDiarioById(parteDiarioId).observe(this, Observer { d ->
            if (d != null) {
                p = d
            }
        })
    }

    private fun menssage() {
        parteDiarioViewModel.error.observe(this, Observer { m ->
            Util.toastMensaje(this, m)
        })
    }

    private fun success() {
        parteDiarioViewModel.success.observe(this, Observer { m ->
            Util.toastMensaje(this, m)
            finish()
        })
    }
}