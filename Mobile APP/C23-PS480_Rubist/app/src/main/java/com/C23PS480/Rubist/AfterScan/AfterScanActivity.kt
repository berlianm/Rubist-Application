package com.C23PS480.Rubist.AfterScan

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.C23PS480.Rubist.API.Response.FileUploadResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.Utils.reduceFileImage
import com.C23PS480.Rubist.databinding.ActivityAfterScanBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AfterScanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAfterScanBinding
    private var getFile: File? = null

    companion object {
        var Image : File? = null
        var jenisSampah : String? = null
        var deskripsi : String? = null
        var dampak : String? = null
        var pembuangan : String? = null
        var daurUlang : String? = null
        var caraDaurUlang : String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoading(true)

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight=200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        getFile = Image
        val result = BitmapFactory.decodeFile(Image?.path)
        binding.ivPriviewImage.setImageBitmap(result)

        uploadImage()

//        binding.tvJenisSampah.text = jenisSampah
//        binding.tvDesc.text = deskripsi
//        binding.tvDampakLingkungan.text = dampak
//        binding.tvPembuangan.text = pembuangan
//        binding.tvDaurUlang.text = daurUlang
//        binding.tvCaraDaurUlang.text = caraDaurUlang

    }


    private fun uploadImage() {
        setLoading(true)

        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            val service = ApiConfig.getApiServiceML().uploadImage(imageMultipart)

            service.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setLoading(false)
                            binding.tvJenisSampah.text = responseBody.Jenis_Sampah
                            binding.tvDesc.text = responseBody.Deskripsi
                            binding.tvDampakLingkungan.text = responseBody.Dampak_Lingkungan
                            binding.tvPembuangan.text = responseBody.Pembuangan
                            binding.tvDaurUlang.text = responseBody.Daur_Ulang
                            binding.tvCaraDaurUlang.text = responseBody.Cara_Daur_Ulang
                        }else{
                            setLoading(true)
                            Toast.makeText(this@AfterScanActivity, response.body()?.error, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    setLoading(true)
                    Toast.makeText(this@AfterScanActivity, t.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            })


        } else {
            setLoading(false)
            Toast.makeText(
                this@AfterScanActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun setLoading(isLoading : Boolean){
        if (isLoading){
            binding.animationView.visibility = View.VISIBLE
            binding.animationView.elevation = 10f
        }else {
            binding.animationView.visibility = View.GONE
        }
    }
}