package com.dsige.dsigeproyectos.ui.activities.engie

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_solicitud_material_form.*

class SolicitudMaterialFormActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_material_form)

        val b = intent.extras
        if (b != null) {
            bindTabLayout(
                b.getInt("solicitudId"),
                b.getInt("tipoMaterialSolicitud"),
                b.getInt("tipoSolicitudId"),
                b.getString("title")!!,
                b.getString("subTitle")!!
            )
        }
    }

    private fun bindTabLayout(
        solicitudId: Int,
        tipoMaterialSolicitud: Int,
        tipoSolicitudId: Int,
        title: String,
        subTitle: String
    ) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = title
        supportActionBar!!.subtitle = subTitle

        if (tipoSolicitudId == 2) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorAccent, theme)))
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1E))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3E))
        if (tipoMaterialSolicitud == 2) {
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_camera))
            tabLayout.getTabAt(2)!!.icon!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            tabLayout.setTabWidthAsWrapContent(2)
        }

        val tabLayoutAdapter = TabLayoutAdapter.TabLayoutSolicitudAdapter(
            supportFragmentManager,
            tabLayout.tabCount,
            solicitudId, tipoMaterialSolicitud, tipoSolicitudId
        )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager.currentItem = position
                Util.hideKeyboard(this@SolicitudMaterialFormActivity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
        val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.layoutParams = layoutParams
    }
}