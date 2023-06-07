package com.C23PS480.Rubist.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.Login.LoginActivity
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.FragmentProfileBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class Profile : Fragment()  {

    private lateinit var mainViewModel : MainViewModel
    private var _binding:FragmentProfileBinding? = null;
    private val binding get() = _binding!!;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[MainViewModel::class.java]

        setupViewModel()
//        val btnLogout : Button = view.findViewById(R.id.btn_logout)
//        btnLogout.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

    private fun setupViewModel() {
        mainViewModel.getUser().observe(requireActivity()) { user ->
           binding.tvProfileName.text = user.name
           binding.tvProfileEmail.text = user.email
        }
    }


//    private fun logoutDialog() {
//        val dialogMessage = getString(R.string.logout_msg)
//        val dialogTitle = getString(R.string.logout)
//
//
//        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
//        alertDialogBuilder.setTitle(dialogTitle)
//
//        alertDialogBuilder
//            .setMessage(dialogMessage)
//            .setCancelable(false)
//            .setPositiveButton(getString(R.string.yes)) { _, _ ->
//              mainViewModel.logout()
//            }
//            .setNegativeButton(getString(R.string.No)) { dialog, _ -> dialog.cancel() }
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }
//
//    override fun onClick(v: View?) {
//        logoutDialog()
//    }


}