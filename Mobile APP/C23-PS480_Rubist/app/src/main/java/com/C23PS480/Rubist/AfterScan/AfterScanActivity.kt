package com.C23PS480.Rubist.AfterScan

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.C23PS480.Rubist.databinding.ActivityAfterScanBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AfterScanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAfterScanBinding

    companion object {
        var Image : File? = null
        var jenisSampah : String? = null
        var dampak : String? = null
        var pembuangan : String? = null
        var daurUlang : String? = null
        var caraDaurUlang : String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight=400
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val result = BitmapFactory.decodeFile(Image?.path)
        binding.ivPriviewImage.setImageBitmap(result)

        if (jenisSampah != null) {
            binding.tvJenisSampah.text = jenisSampah
        }




    }
}