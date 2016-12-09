package pl.krk.droidcon.workshops.login

interface Login {

    interface FacebookButtonProvider

    interface FacebookLoginCreator {
        fun create(loginController: pl.krk.droidcon.workshops.login.LoginController): FacebookButtonProvider
    }

    interface FacebookLoginCallbacks

    interface View {
        fun addFacebookButton(fbButtonProvider: FacebookButtonProvider)
        fun showLoginFailsError()
    }

    interface Api {
        fun loginWithFbToken(token: String)
    }
}