package com.space.astronaut.astronautlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.space.astronaut.AstronautApplication
import com.space.astronaut.R
import com.space.astronaut.model.Results
import com.space.astronaut.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AstronautListActivity : AppCompatActivity() {
    private lateinit var astronautListViewModel: AstronautListViewModel

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var astronautInfoAdapter: AstronautInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as AstronautApplication).appComponent.astronautListComponent().create()
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        astronautListViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(AstronautListViewModel::class.java)

        setRecyclerView()
        astronautListViewModel.getAstronout().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.hide()
                    it.data?.let { astronautList ->
                        astronautInfoAdapter.setUserInfoList(
                            astronautList.results
                        )
                    }
                }
                Status.LOADING -> {
                    progressBar.show()
                }
                Status.ERROR -> {
                    progressBar.hide()
                    displayError(it.message.toString())
                    // Alert view implementaion
                }
            }
        })

        getAstronautInfoList();
    }

    private fun setRecyclerView() {
        reposListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = astronautInfoAdapter
            astronautInfoAdapter.setClickListener(object : AstronauntInfoClickListener {
                override fun userInfoListClicked(result: Results) {

                }
            });
        }

    }


    private fun getAstronautInfoList() {
        astronautListViewModel.fetchAstronoutList()
    }

    private fun displayError(errorMessage: String) {
            var alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle(getString(R.string.error_title))
            alertBuilder.setMessage(errorMessage)
            alertBuilder.setPositiveButton(getString(R.string.ok_button), null)
            alertBuilder.show()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.my_options_menu, menu)

            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
            R.id.action_custom_button -> {
                astronautListViewModel.getSortedAstronout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


}

