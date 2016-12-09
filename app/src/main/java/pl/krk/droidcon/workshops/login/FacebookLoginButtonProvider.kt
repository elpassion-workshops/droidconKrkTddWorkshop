package pl.krk.droidcon.workshops.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import pl.krk.droidcon.workshops.R

interface FacebookLoginButtonProvider {

    fun createButton(onSucces: FacebookCallbackSuccess, onError: FacebookCallbackError): (ViewGroup) -> View

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

        override fun createButton(onSucces: FacebookCallbackSuccess, onError: FacebookCallbackError): (ViewGroup) -> View {
            return { createButtonImpl(onSucces, onError, it) }
        }

        private fun createButtonImpl(onSuccess: FacebookCallbackSuccess,
                                     onError: FacebookCallbackError,
                                     viewGroup: ViewGroup): LoginButton {
            val button = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.facebook_button, viewGroup, false) as LoginButton
            button.setReadPermissions("email")
            button.registerCallback(callbackManager, object : com.facebook.FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    onSuccess.onFacebookSuccess(loginResult.accessToken.token)
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    onError.onFacebookFail()
                }
            })
            return button
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)

    interface FacebookCallbackError {
        fun onFacebookFail()
    }

    interface FacebookCallbackSuccess {
        fun onFacebookSuccess(token: String)
    }
}