package com.cp23ps480.rubist.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cp23ps480.rubist.MainViewModel
import com.cp23ps480.rubist.R
import com.cp23ps480.rubist.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch


class Profile : Fragment(), View.OnClickListener  {

    private lateinit var mainViewModel : MainViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val btnLogout : Button = view.findViewById(R.id.logout)
        btnLogout.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    private fun logoutDialog() {
        val dialogMessage = getString(R.string.logout_msg)
        val dialogTitle = getString(R.string.logout)


        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle(dialogTitle)

        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
              mainViewModel.logout()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(v: View?) {
        logoutDialog()
    }


}