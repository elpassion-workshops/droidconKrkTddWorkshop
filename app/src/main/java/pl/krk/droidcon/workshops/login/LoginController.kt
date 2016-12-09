package pl.krk.droidcon.workshops.login

import rx.Observable
import rx.Subscription

import java.util.regex.Pattern

class LoginController(val api: Login.Api,
                      val view: Login.View,
                      val sharedPreferences: UserSharedPreferences,
                      val facebook: FacebookLoginButtonProvider) : FacebookLoginButtonProvider.FacebookCallbackSuccess,
        FacebookLoginButtonProvider.FacebookCallbackError {

    private var subscription: Subscription? = null

    fun onCreate() {
        view.setupFacebookButton(
                facebook.createButton(
                        onSucces = this,
                        onError = this)
        )
    }

    fun onLogin(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) {
            view.showEmptyCredentialError()
        } else if (!login.matches(VALID_EMAIL_ADDRESS_REGEX)) {
            view.showInvalidEmailError()
        } else {
            callApi(login, password)
        }
    }

    private fun callApi(login: String, password: String) {
        subscribeTo(api.login(login, password))
    }

    override fun onFacebookSuccess(token: String) {
        subscribeTo(api.loginWithFacebook(token))
    }

    fun onDestroy() {
        subscription?.unsubscribe()
    }

    private fun subscribeTo(apiCall: Observable<User>) {
        subscription = apiCall
                .doOnSubscribe { view.showLoader() }
                .doOnUnsubscribe { view.hideLoader() }
                .doOnNext { sharedPreferences.saveUser(it) }
                .subscribe({
                    view.openNextScreen()
                }, {
                    view.showLoginFailedError()
                })
    }

    override fun onFacebookFail() {
        view.showLoginFailedError()
    }

    companion object {
        private val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+").toRegex()

    }
}