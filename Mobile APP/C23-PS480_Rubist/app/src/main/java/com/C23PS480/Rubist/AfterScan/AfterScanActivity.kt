package com.C23PS480.Rubist.AfterScan

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.C23PS480.Rubist.API.Response.FileUploadResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.Utils.createCustomTempFile
import com.C23PS480.Rubist.Utils.reduceFileImage
import com.C23PS480.Rubist.Utils.uriToFile
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
    private lateinit var currentPhotoPath: String

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

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
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


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            uploadImage()
            binding.ivPriviewImage.setImageBitmap(result)

        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)


        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AfterScanActivity,
                "com.C23PS480.Rubist",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AfterScanActivity)
                getFile = myFile
                uploadImage()
                binding.ivPriviewImage.setImageURI(uri)
            }

        }
    }
}