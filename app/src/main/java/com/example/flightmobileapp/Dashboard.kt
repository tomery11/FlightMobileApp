package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private var rudderSeekbarView: SeekBar? =null
    private var throttleSeekbarView: SeekBar? =null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        rudderSeekbarView = this.rudderSeekbar
        rudderSeekbarView!!.setOnSeekBarChangeListener(this)
        throttleSeekbarView = this.throttleSeekbar
        throttleSeekbarView!!.setOnSeekBarChangeListener(this)

    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }
}