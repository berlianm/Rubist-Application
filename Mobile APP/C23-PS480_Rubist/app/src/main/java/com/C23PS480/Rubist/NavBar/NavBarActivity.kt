package com.C23PS480.Rubist.NavBar

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.ActivityNavBarBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class NavBarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNavBarBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        binding.signoutMenu.setOnClickListener{
            logoutDialog()
        }
    }

    private fun logoutDialog() {
        val dialogMessage = getString(R.string.logout_msg)
        val dialogTitle = getString(R.string.logout)


        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)

        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                mainViewModel.logout()
                finish()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}