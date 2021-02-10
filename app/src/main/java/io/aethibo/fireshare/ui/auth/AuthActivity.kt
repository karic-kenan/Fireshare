package io.aethibo.fireshare.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aethibo.fireshare.MainActivity
import io.aethibo.fireshare.R
import io.aethibo.fireshare.framework.utils.FirebaseUtil.auth

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        checkUserAuth()
    }

    private fun checkUserAuth() {
        if (auth.currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}