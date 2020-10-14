package com.dsige.dsigeproyectos.ui.activities.logistica

import android.os.Bundle
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_requerimiento_form.*

class RequerimientoFormActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requerimiento_form)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getInt("id"), b.getString("usuarioId")!!)
        }
    }

    private fun bindUI(id: Int, u: String) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Solicitud de Requerimiento"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1E))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3E))

        val tabLayoutAdapter = TabLayoutAdapter.TabLayoutRequerimientoAdapter(
            supportFragmentManager, tabLayout.tabCount, id, u
        )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager.currentItem = position
                Util.hideKeyboard(this@RequerimientoFormActivity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}