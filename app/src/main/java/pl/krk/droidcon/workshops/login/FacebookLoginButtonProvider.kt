package pl.krk.droidcon.workshops.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import pl.krk.droidcon.workshops.R

interface FacebookLoginButtonProvider {

    fun addToContainer(viewGroup: ViewGroup, onSuccess: () -> Unit, onError: () -> Unit)

    companion object {
        private val instance by lazy { Impl() }

        fun get() = overided ?: instance

        var overided: FacebookLoginButtonProvider? = null
    }

    class Impl : FacebookLoginButtonProvider {
        private val callbackManager by lazy { com.facebook.CallbackManager.Factory.create() }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

        override fun addToContainer(viewGroup: ViewGroup, onSuccess: () -> Unit, onError: () -> Unit) {
            val button = LayoutInflater.from(viewGroup.context).inflate(R.layout.facebook_button, viewGroup, false) as LoginButton
            button.setReadPermissions("email")
            button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    onSuccess()
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    onError()
                }
            })
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
}