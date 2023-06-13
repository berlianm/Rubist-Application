package com.C23PS480.Rubist.OnBoard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.C23PS480.Rubist.MainActivity
import com.C23PS480.Rubist.R

class OnBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.transitionToStart()

        val Start = findViewById<Button>(R.id.buttonMenu1)
        Start.setOnClickListener{
            startActivity(Intent(this@OnBoardActivity, MainActivity::class.java))
        }
    }
}