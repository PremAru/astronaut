package com.space.astronaut.astronautinfo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.space.astronaut.AstronautApplication
import com.space.astronaut.R
import com.space.astronaut.databinding.ActivityAstronoutInfoBinding
import com.space.astronaut.model.Results
import com.space.astronaut.utils.Constants
import com.space.astronaut.utils.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_astronout_info.*
import javax.inject.Inject

class AstronautInfoActivity : AppCompatActivity() {

    lateinit var astronautInfoViewModel: AstronautInfoViewModel

    @Inject
    internal lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityAstronoutInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as AstronautApplication).appComponent.astronautInfoComponent().create()
            .inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_astronout_info)

        astronautInfoViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(AstronautInfoViewModel::class.java)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        val astronautInfo = intent.getSerializableExtra(Constants.KEY_RESULT) as Results
        binding.astronautName = astronautInfo.name
        binding.astronautNationality = astronautInfo.nationality


        Picasso.get().load(astronautInfo.profile_image_thumbnail)
            .placeholder(R.drawable.loading_image)
            .into(astronautInfoImageView)
        astronautInfoViewModel.getAstronautDetails().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    infoProgressBar.hide()
                    it.data?.let { astronaut ->
                        dateOfBirthTextView.text =
                            astronaut.date_of_birth
                        bioTextView.text =
                            astronaut.bio
                    }
                }
                Status.LOADING -> {
                    infoProgressBar.show()
                }
                Status.ERROR -> {
                    infoProgressBar.hide()
                    displayError(it.message.toString())
                }
            }
        })

        astronautInfoViewModel.fetchAstronautDetails(astronautInfo.id)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayError(errorMessage: String) {
        var alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle(getString(R.string.error_title))
        alertBuilder.setMessage(errorMessage)
        alertBuilder.setPositiveButton(getString(R.string.ok_button), null)
        alertBuilder.show()
    }
}