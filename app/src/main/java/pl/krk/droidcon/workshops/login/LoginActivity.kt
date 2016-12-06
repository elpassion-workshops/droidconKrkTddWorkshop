package pl.krk.droidcon.workshops.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R
import rx.Observable

class LoginActivity : AppCompatActivity(), Login.View {
    private val controller by lazy {
        LoginController(object : Login.Api {
            override fun login(login: String, password: String): Observable<Unit> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginLoginButton.setOnClickListener {
            controller.onLogin(loginLoginInput.text.toString(), loginPasswordInput.text.toString())
        }
    }

    override fun hideLoader() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoader() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openNextScreen() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginFailedError() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyCredentialError() {
        loginErrorMessage.setText(R.string.loginEmptyCredentialError)
    }

    override fun showInvalidEmailError() {

    }


}
