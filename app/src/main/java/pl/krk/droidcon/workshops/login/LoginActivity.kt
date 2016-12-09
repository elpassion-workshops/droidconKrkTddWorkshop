package pl.krk.droidcon.workshops.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        facebookButton.setOnClickListener { loginErrorMessage.visibility = VISIBLE }
        Login.LoginApiProvider.override.loginWithFbToken("")
    }
}
