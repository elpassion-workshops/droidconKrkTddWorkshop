package pl.krk.droidcon.workshops.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R
import pl.krk.droidcon.workshops.next.NextScreenActivity

class LoginActivity : AppCompatActivity(), Login.View {
    private val controller by lazy {
        LoginController(Login.ApiProvider.get(), this, object : UserSharedPreferences {
            override fun saveUser(user: User) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginLoginButton.setOnClickListener {
            controller.onLogin(loginLoginInput.text.toString(), loginPasswordInput.text.toString())
        }
    }

    override fun hideLoader() {
    }

    override fun showLoader() {
    }

    override fun openNextScreen() {
        startActivity(Intent(this, NextScreenActivity::class.java))
    }

    override fun showLoginFailedError() {
        loginErrorMessage.setText(R.string.loginCallFailedError)
    }

    override fun showEmptyCredentialError() {
        loginErrorMessage.setText(R.string.loginEmptyCredentialError)
    }

    override fun showInvalidEmailError() {
        loginErrorMessage.setText(R.string.invalidEmailError)
    }


}
