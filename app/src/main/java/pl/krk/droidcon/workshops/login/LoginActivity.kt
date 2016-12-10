package pl.krk.droidcon.workshops.login

import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R
import rx.Observable
import rx.schedulers.Schedulers

class LoginActivity : AppCompatActivity(), Login.View {

    private val loginController by lazy {
        LoginController(LoginProivder.get(), this, object : Login.UserStorage {
            override fun saveUserData(user: User) {
            }
        }, Schedulers.immediate(), Schedulers.immediate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            loginController.onLogin(email = loginEmailInput.text.toString(), password = loginPasswordInput.text.toString())
        }
    }

    override fun showError() {
        loginError.visibility = View.VISIBLE
    }

    override fun hideError() {
        loginError.visibility = View.INVISIBLE
    }

    override fun gotoHomeScreen() {

    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }
}
object LoginProivder {
    val realImplementation: Login.Api by lazy {
        object : Login.Api {
            override fun login(login: String, password: String): Observable<User> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }


    var override: Login.Api ?= null

    fun get(): Login.Api {
        return override ?: realImplementation
    }
}