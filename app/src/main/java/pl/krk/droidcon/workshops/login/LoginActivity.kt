package pl.krk.droidcon.workshops.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.login_activity.*
import pl.krk.droidcon.workshops.R

class LoginActivity : AppCompatActivity(), Login.View {

    private val controller: LoginController by lazy {
        LoginController(this, Login.FacebookLoginCreatorProvider.override, Login.LoginApiProvider.override)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        controller.onCreate()
    }

    override fun showLoginFailsError() {
        loginErrorMessage.visibility = VISIBLE
    }

    override fun addFacebookButton(fbButtonProvider: Login.FacebookButtonProvider) {
        loginActivity.addView(fbButtonProvider.getButton(this))
    }
}
