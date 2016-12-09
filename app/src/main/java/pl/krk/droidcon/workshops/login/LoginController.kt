package pl.krk.droidcon.workshops.login

class LoginController(private val view: Login.View,
                      private val facebookLoginCreator: Login.FacebookLoginCreator,
                      private val api: Login.Api) : Login.FacebookLoginCallbacks {

    fun onCreate() {
        view.addFacebookButton(facebookLoginCreator.create(this))
    }

    override fun onFacebookLoginSuccess(token: String) {
        api.loginWithFbToken(token)
    }

    override fun onFacebookLoginError() {
        view.showLoginFailsError()
    }

}