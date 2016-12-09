package pl.krk.droidcon.workshops.login

import android.content.Context

interface Login {

    interface FacebookButtonProvider {
        fun getButton(context: Context): android.view.View
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
        lateinit var override: Api
    }

    object FacebookLoginCreatorProvider {
        lateinit var override: FacebookLoginCreator
    }
}