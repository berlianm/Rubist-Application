package com.C23PS480.Rubist

import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.API.Response.FileUploadResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.AfterScan.AfterScanActivity
import com.C23PS480.Rubist.Fragment.AddPost
import com.C23PS480.Rubist.Fragment.Community
import com.C23PS480.Rubist.Fragment.Home
import com.C23PS480.Rubist.Fragment.Profile
import com.C23PS480.Rubist.Login.LoginActivity
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.Utils.createCustomTempFile
import com.C23PS480.Rubist.Utils.reduceFileImage
import com.C23PS480.Rubist.databinding.ActivityMainBinding

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]


        setupViewModel()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.camera.setOnClickListener{
            startTakePhoto()
        }

    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    private fun NavBar(){
        replaceFragment(Home())
        binding.BottomNavView.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.community -> replaceFragment(Community())
                R.id.add -> replaceFragment(AddPost())//startActivity(Intent(this, AddPostActivity::class.java))
                R.id.profile-> replaceFragment(Profile())
                else -> {}
            }
            true
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                NavBar()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

//            uploadImage()
            AfterScanActivity.Image = getFile
            startActivity(Intent(this, AfterScanActivity::class.java))


//            myFile.let { file ->
////          Silakan gunakan kode ini jika mengalami perubahan rotasi
////          rotateFile(file)
//                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
//            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.C23PS480.Rubist",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun uploadImage() {
//        setLoading(true)


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
//                        setLoading(false)
                        val responseBody = response.body()
                        if (responseBody != null ) {
                            AfterScanActivity.jenisSampah = responseBody.Jenis_Sampah
                            AfterScanActivity.deskripsi = responseBody.Deskripsi
                            AfterScanActivity.dampak = responseBody.Dampak_Lingkungan
                            AfterScanActivity.pembuangan = responseBody.Pembuangan
                            AfterScanActivity.daurUlang = responseBody.Daur_Ulang
                            AfterScanActivity.caraDaurUlang = responseBody.Cara_Daur_Ulang
                        }
                    }
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                    setLoading(false)
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })


        } else {
//            setLoading(false)
            Toast.makeText(this@MainActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

}