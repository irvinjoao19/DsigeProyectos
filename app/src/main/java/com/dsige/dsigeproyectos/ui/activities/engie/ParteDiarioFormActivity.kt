package com.dsige.dsigeproyectos.ui.activities.engie

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.LinearLayout
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.adapters.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_parte_diario_form.*

class ParteDiarioFormActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parte_diario_form)
        val b = intent.extras
        if (b != null) {
            bindToolbar()
            bindTabLayout(b.getInt("parteDiarioId"))
        }
    }

    private fun bindToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nuevo Parte Diario"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun bindTabLayout(parteDiarioId : Int) {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1E))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2E))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3E))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_camera))
        tabLayout.getTabAt(3)!!.icon!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        tabLayout.setTabWidthAsWrapContent(3)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab5))

        val tabLayoutAdapter = TabLayoutAdapter.TabLayoutBaremoAdapter(
            supportFragmentManager,
            tabLayout.tabCount,
            parteDiarioId, 1
        )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager.currentItem = position
                Util.hideKeyboard(this@ParteDiarioFormActivity)
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