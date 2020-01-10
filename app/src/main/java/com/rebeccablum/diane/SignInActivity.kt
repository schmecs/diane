package com.rebeccablum.diane

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.rebeccablum.diane.databinding.ActivitySignInBinding
import com.rebeccablum.diane.viewmodels.SignInViewModel
import com.rebeccablum.diane.viewmodels.ViewModelFactory
import java.util.*
import kotlin.concurrent.schedule

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val DRIVE_FILE_SCOPE = "https://www.googleapis.com/auth/drive.file"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val REQUEST_SIGN_IN = 300
    }

    private val viewModel: SignInViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(SignInViewModel::class.java)
    }

    private var permissionsAccepted = false

    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()

        setSignInClient()
    }

    override fun onStart() {
        super.onStart()

        checkAudioPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if (!permissionsAccepted) {
            finish()
        } else {
            checkForLoggedInAccount()
        }
    }

    private fun checkAudioPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )

        } else {
            // Permission has already been granted
            checkForLoggedInAccount()
        }
    }

    private fun checkForLoggedInAccount() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.let {
            onLoginSuccess(it)
        } ?: viewModel.isButtonEnabled.set(true)
    }

    private fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.viewModel = viewModel
        binding.googleSignInButton.setOnClickListener { signIn() }
    }

    private fun setSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DRIVE_FILE_SCOPE))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            completedTask.getResult(ApiException::class.java)?.let {
                onLoginSuccess(it)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInActivity", "signInResult:failed code=" + e.statusCode)
            updateUiForError()
        }

    }

    private fun onLoginSuccess(account: GoogleSignInAccount) {
        updateUiForLoggedInAccount(account.displayName)

        Timer().schedule(2000) {
            navigateToHomeScreen()
        }
    }

    private fun updateUiForLoggedInAccount(displayName: String?) {
        displayName?.let {
            viewModel.name.set(it)
        }
        viewModel.isLoggedIn.set(true)
        viewModel.isButtonEnabled.set(false)
    }

    private fun updateUiForError() {
        viewModel.isInErrorState.set(true)
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}