package com.C23PS480.Rubist.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.C23PS480.Rubist.NavBar.NavBarActivity
import com.C23PS480.Rubist.R


class Home : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sideBar: ImageButton = view.findViewById(R.id.imageButton)
        sideBar.setOnClickListener {
            // Pindah ke activity lain
            val intent = Intent(activity, NavBarActivity::class.java)
            startActivity(intent)

        }
        val community: Button = view.findViewById(R.id.button)
        community.setOnClickListener{
            val newFragment = Community()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, newFragment)
            transaction.commit()
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