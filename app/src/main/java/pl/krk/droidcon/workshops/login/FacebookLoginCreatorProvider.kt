package pl.krk.droidcon.workshops.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import pl.krk.droidcon.workshops.R

object FacebookLoginCreatorProvider {

    private val callbackManager = CallbackManager.Factory.create()

    var override = object : Login.FacebookLoginCreator {
        override fun create(callbacks: Login.FacebookLoginCallbacks): Login.FacebookButtonProvider {
            return object : Login.FacebookButtonProvider {
                override fun getButton(viewGroup: ViewGroup): View {
                    return LayoutInflater.from(viewGroup.context).inflate(R.layout.facebook_button, viewGroup, false).apply {
                        (this as LoginButton).registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                            override fun onError(error: FacebookException?) {
                                callbacks.onFacebookLoginError()
                            }

                            override fun onCancel() {
                            }

                            override fun onSuccess(result: LoginResult) {
                                callbacks.onFacebookLoginSuccess(result.accessToken.token)
                            }
                        })
                    }
                }
            }
        }
    }
}