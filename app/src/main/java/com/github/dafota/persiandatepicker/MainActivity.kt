package com.github.dafota.persiandatepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.dafota.persiandatepicker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }


}