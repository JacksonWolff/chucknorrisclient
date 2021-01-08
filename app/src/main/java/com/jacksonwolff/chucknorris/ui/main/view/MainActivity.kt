package com.jacksonwolff.chucknorris.ui.main.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnStart
import androidx.lifecycle.ViewModelProviders
import com.jacksonwolff.chucknorris.R
import com.jacksonwolff.chucknorris.data.api.ApiHelper
import com.jacksonwolff.chucknorris.data.api.RetrofitBuilder
import com.jacksonwolff.chucknorris.data.model.ChuckNorrisFact
import com.jacksonwolff.chucknorris.ui.base.MainViewModel
import com.jacksonwolff.chucknorris.ui.base.ViewModelFactory
import com.jacksonwolff.chucknorris.ui.detail.DetailActivity
import com.jacksonwolff.chucknorris.ui.search.view.SearchActivity
import com.jacksonwolff.chucknorris.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var factCategories: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupUI()
        setupCategoryObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun promptAgeDialog(category: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.age_check_dialog_title))
        builder.setMessage(getString(R.string.age_dialog_message))

        builder.setNegativeButton(getString(R.string.age_dialog_negative_label)) { _, _ ->
            Toast.makeText(
                applicationContext,
                getString(R.string.warn_under_age_message),
                Toast.LENGTH_LONG
            ).show()
        }
        builder.setPositiveButton(getString(R.string.age_dialog_positive_label)) { _, _ ->


            Toast.makeText(
                applicationContext,
                getString(R.string.over_age_toast_message), Toast.LENGTH_SHORT
            ).show()

            setupCategoryFactObserver(category)
        }

        builder.create().show()
    }


    private fun createCategoriesDialog(categories: List<String>) {
        return let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.category_dialog_title))
                .setItems(
                    categories.toTypedArray()
                ) { _, which ->

                    val currentCategory = categories[which]
                    if (currentCategory == "explicit") {
                        promptAgeDialog(currentCategory)
                    } else {
                        setupCategoryFactObserver(currentCategory)
                    }
                }
            builder.create().show()

        }
    }


    private fun setupUI() {


        val imgBgScaleX = PropertyValuesHolder.ofFloat(SCALE_X, 1.0f)
        val imgBgScaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 1.0f)
        val imgBgTranslateY = PropertyValuesHolder.ofFloat(TRANSLATION_Y, 0.0f)
        val imgBgAlpha = PropertyValuesHolder.ofFloat(ALPHA, 0.4f)
        val barAnim = ObjectAnimator
            .ofPropertyValuesHolder(imgBg, imgBgScaleX, imgBgScaleY, imgBgTranslateY, imgBgAlpha)
            .setDuration(800)

        barAnim.doOnStart {

            categoriesBt.setOnClickListener {
                createCategoriesDialog(factCategories)
            }

            randomBt.setOnClickListener {
                setupRandomFactObserver()
            }

            searchBtn.setOnClickListener {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            val vcScaleX = PropertyValuesHolder.ofFloat(SCALE_X, 1.0f)
            val vcScaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 1.0f)
            val vcAlpha = PropertyValuesHolder.ofFloat(ALPHA, 1.0f)
            val layoutAnimator = ObjectAnimator
                .ofPropertyValuesHolder(viewContainer, vcScaleX, vcScaleY, vcAlpha)
                .setDuration(1500)

            layoutAnimator.startDelay = 500
            layoutAnimator.start()


        }

        barAnim.start()


    }


    private fun setupCategoryObserver() {

        viewModel.getCategories().observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = GONE
                        resource.data?.let { categories -> factCategories = categories }
                    }
                    Status.ERROR -> {

                        progressBar.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = VISIBLE
                        recyclerView.visibility = GONE
                    }
                }
            }
        })
    }


    private fun setupRandomFactObserver() {
        viewModel.getRandomFact().observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = GONE
                        resource.data?.let { fact -> retrieveFact(fact) }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = VISIBLE
                        recyclerView.visibility = GONE
                    }
                }
            }
        })
    }

    private lateinit var currentFact: ChuckNorrisFact
    private fun retrieveFact(fact: ChuckNorrisFact) {
        currentFact = fact
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("fact", currentFact)
        startActivity(intent)

    }

    private fun setupCategoryFactObserver(category: String) {
        viewModel.getCategoryFact(category).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = GONE
                        resource.data?.let { fact -> retrieveFact(fact) }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = VISIBLE
                        recyclerView.visibility = GONE
                    }
                }
            }
        })
    }

}