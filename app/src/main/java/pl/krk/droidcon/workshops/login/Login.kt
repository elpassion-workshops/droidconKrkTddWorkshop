package pl.krk.droidcon.workshops.login

import android.view.ViewGroup

interface Login {

    interface FacebookButtonProvider {
        fun getButton(viewGroup: ViewGroup): android.view.View
    }

    interface FacebookLoginCreator {
        fun create(callbacks: FacebookLoginCallbacks): FacebookButtonProvider
    }

    interface FacebookLoginCallbacks {
        fun onFacebookLoginSuccess(token: String)

        fun onFacebookLoginError()
    }

    interface View {
        fun addFacebookButton(fbButtonProvider: FacebookButtonProvider)
        fun showLoginFailsError()
    }

    interface Api {
        fun loginWithFbToken(token: String)
    }

    object LoginApiProvider {
        var override: Api = object : Api {
            override fun loginWithFbToken(token: String) {

            }
        }
    }

}