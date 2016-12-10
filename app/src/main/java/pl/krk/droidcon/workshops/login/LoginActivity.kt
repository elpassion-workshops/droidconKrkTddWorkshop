package pl.krk.droidcon.workshops.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R
import rx.schedulers.Schedulers

class LoginActivity : AppCompatActivity(), Login.View {
    private val loginController by lazy {
        LoginController(LoginApiProvider.get(), this, object : Login.UserStorage {
            override fun saveUserData(user: User) {
            }
        }, Schedulers.immediate(), Schedulers.immediate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            loginController.onLogin(loginEmailInput.text.toString(), loginPasswordInput.text.toString())
        }
    }

    override fun showError(errorStringResource: Int) {
        loginError.setText(errorStringResource)
        loginError.visibility = View.VISIBLE
    }

    override fun gotoHomeScreen() {
    }

    override fun disableLoginButton() {
        loginButton.isEnabled = false
    }

    override fun showLoader() {
        loginProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        loginProgressBar.visibility = View.GONE
    }
}
