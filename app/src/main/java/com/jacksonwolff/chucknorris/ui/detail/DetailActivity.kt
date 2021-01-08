package com.jacksonwolff.chucknorris.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jacksonwolff.chucknorris.R
import com.jacksonwolff.chucknorris.data.model.ChuckNorrisFact
import com.jacksonwolff.chucknorris.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    private lateinit var chuckNorrisFact: ChuckNorrisFact
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail)

        chuckNorrisFact = intent.getSerializableExtra("fact") as ChuckNorrisFact
        binding.chuckNorrisFact = chuckNorrisFact

        Glide.with(this).load(chuckNorrisFact.iconUrl).into(iconImageView)

    }
}