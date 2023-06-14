package com.C23PS480.Rubist.Fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.API.Response.AddPostResponse
import com.C23PS480.Rubist.API.Retrofit.ApiConfig
import com.C23PS480.Rubist.MainViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.Utils.createCustomTempFile
import com.C23PS480.Rubist.Utils.uriToFile
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.FragmentAddPostBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
@Suppress("DEPRECATION")
class AddPost : Fragment() {
    private var getFile: File? = null
    private lateinit var binding: FragmentAddPostBinding
    private lateinit var mainViewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View{
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(requireActivity()) { user ->
            userId = user.uid
        }

        if (!allPermissionsGranted()){
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.ivCamera.setOnClickListener{ startTakePhoto() }
        binding.tvGelery.setOnClickListener { startGallery() }
        binding.tvUpload.setOnClickListener { UploadDialog() }


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val MAXIMAL_SIZE = 1000000

        var userId :String? = null
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireContext().applicationContext).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
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
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                // Silakan gunakan kode ini jika mengalami perubahan rotasi
//                rotateFile(file)
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun UploadDialog() {
        val dialogMessage = getString(R.string.Posting)
        val dialogTitle = getString(R.string.AddPost)


        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle(dialogTitle)

        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                uploadImage()

            }
            .setNegativeButton(getString(R.string.No)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun uploadImage(){
        if (getFile != null){
            val file = reduceFileImage(getFile as File)

            val description = binding.etDesc.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val title = binding.etTitle.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val userId = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val apiService = ApiConfig.getApiService()
            val uploadImageRequest = apiService.addPost(title, description, userId, imageMultipart)
            uploadImageRequest.enqueue(object : Callback<AddPostResponse>{
                override fun onResponse(
                    call: Call<AddPostResponse>,
                    response: Response<AddPostResponse>
                ) {
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if( responseBody != null ){
                            Toast.makeText(
                                requireContext(),
                                "Post Uploaded",
                                Toast.LENGTH_SHORT
                            ).show()
//                            navigateBackToStoryList()
                        }
                    } else{
                        Toast.makeText(
                            requireContext(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else{
            Toast.makeText(
                requireContext(),
                "insert image first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


//    private fun navigateBackToStoryList() {
//        // Implement code to navigate back to the list of stories
//        findNavController().navigateUp()
//    }
}