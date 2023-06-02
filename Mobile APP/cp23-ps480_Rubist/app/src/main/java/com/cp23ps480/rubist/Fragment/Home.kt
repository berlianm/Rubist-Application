package com.cp23ps480.rubist.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.cp23ps480.rubist.MainViewModel
import com.cp23ps480.rubist.NavBar.NavBarActivity
import com.cp23ps480.rubist.R


class Home : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sideBar: ImageButton = view.findViewById(R.id.imageButton)
        sideBar.setOnClickListener {
            // Pindah ke activity lain
            val intent = Intent(activity, NavBarActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}