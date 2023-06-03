package com.C23PS480.Rubist.Register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.Login.LoginActivity
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.R
import com.C23PS480.Rubist.ViewModelFactory
import com.C23PS480.Rubist.databinding.ActivityRegisterBinding
import java.lang.ref.WeakReference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
var weakReference: WeakReference<ActivityRegisterBinding>? = null

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel


    companion object {
        fun isErrorPassword(isError: Boolean) {
            val binding = weakReference?.get()
            binding?.layoutPassword?.isEndIconVisible = !isError
            binding?.btnRegister?.isEnabled = !isError

        }

        var isError = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weakReference = WeakReference(binding)
        setupView()
        setupViewModel()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {

        registerViewModel.Loading.observe(this){
            setLoading(it)
        }

        registerViewModel.msg.observe(this) {
            AlertDialog.Builder(this@RegisterActivity).apply {
                setTitle(if (isError) "Error" else "Yeah!")
                setMessage(it)
                setPositiveButton("OK") { _, _ ->
                    if (!isError) finish()
                }
                create()
                show()
            }
        }
        binding.btnRegister.setOnClickListener {


            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.etName.error = "Masukkan nama anda"
                }
                email.isEmpty() -> {
                    binding.etEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Masukkan password"
                }
                else -> {
                    registerViewModel.register(name, email, password)
                    registerViewModel.isError.observe(this) {
                        if (!it) registerSucces() else registerError()
                    }

                }
            }
        }

        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    fun setLoading(isLoading : Boolean){
        if (isLoading){
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.GONE
        }
    }

    fun registerSucces() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(
                R.string.register_succes_msg
            )
            setPositiveButton("OK") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    fun registerError() {
        AlertDialog.Builder(this).apply {
            setTitle("ERROR!")
            setMessage(
                R.string.register_error_msg
            )
            setPositiveButton("OK") { _, _ ->

            }
            create()
            show()
        }
    }

}