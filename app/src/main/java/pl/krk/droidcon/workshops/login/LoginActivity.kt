package pl.krk.droidcon.workshops.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class LoginActivity : AppCompatActivity(), Login.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun openNextScreen() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun showError() {
        throw UnsupportedOperationException("not implemented")
    }
}
