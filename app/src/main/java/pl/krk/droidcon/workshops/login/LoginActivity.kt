package pl.krk.droidcon.workshops.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.MainActivity
import pl.krk.droidcon.workshops.R
import rx.schedulers.Schedulers

class LoginActivity : AppCompatActivity(), Login.View {
    override fun disableLoginButton() {
        loginButton.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val loginController = LoginController(LoginApiProvider.get(), this, object : Login.UserStorage {
            override fun saveUserData(user: User) {
            }
        }, Schedulers.immediate(), Schedulers.immediate())
        loginButton.setOnClickListener {
            loginController.onLogin(
                    loginEmailInput.text.toString(),
                    loginPasswordInput.text.toString()
            )
        }
    }

    override fun showError() {
        loginError.visibility = View.VISIBLE
    }

    override fun gotoHomeScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showLoader() {
        loginProgressView.visibility = View.VISIBLE
    }

    override fun hideLoader() {
    }
}
