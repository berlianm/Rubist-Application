package com.cp23ps480.rubist.AfterScan

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.cp23ps480.rubist.R
import com.cp23ps480.rubist.databinding.ActivityAfterScanBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AfterScanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAfterScanBinding

    companion object {
        var Image : File? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight=900
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val result = BitmapFactory.decodeFile(Image?.path)
        binding.ivPriviewImage.setImageBitmap(result)


    }
}