package com.jacksonwolff.chucknorris.ui.search.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacksonwolff.chucknorris.R
import com.jacksonwolff.chucknorris.data.api.ApiHelper
import com.jacksonwolff.chucknorris.data.api.RetrofitBuilder
import com.jacksonwolff.chucknorris.data.model.ChuckNorrisFact
import com.jacksonwolff.chucknorris.ui.base.MainViewModel
import com.jacksonwolff.chucknorris.ui.base.ViewModelFactory
import com.jacksonwolff.chucknorris.ui.search.adapter.SearchAdapter
import com.jacksonwolff.chucknorris.utils.Status
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: SearchAdapter
    private lateinit var factView: RecyclerView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupViewModel()
        setupUI()

    }

    var queryTextChangedJob: Job? = null

    private fun setupUI() {
        factView = findViewById(R.id.recyclerView)
        factView.layoutManager = LinearLayoutManager(factView.context)
        factView.setHasFixedSize(true)

        factSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.length!! >= 3) {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(800)
                        setupCategoryFactObserver(newText.toString())
                        delay(200)
                        factSearch.clearFocus()
                        recyclerView.requestFocus()
                    }

                } else {
                    adapter.addFacts(mutableListOf())
                }
                return false
            }

        })

        adapter = SearchAdapter(mutableListOf())
        factView.adapter = adapter

        factSearch.requestFocus()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupCategoryFactObserver(term: String) {
        viewModel.getSearchFacts(term).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { facts -> retrieveList(facts.result) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private lateinit var cFacts: MutableList<ChuckNorrisFact>
    private fun retrieveList(result: MutableList<ChuckNorrisFact>) {

        cFacts = result
        adapter.apply {
            addFacts(result)
            notifyDataSetChanged()
        }

        factView.clearFocus()
        recyclerView.requestFocus()
    }


}