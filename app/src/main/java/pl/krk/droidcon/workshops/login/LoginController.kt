package pl.krk.droidcon.workshops.login

import rx.Subscription
import java.util.regex.Pattern

class LoginController(val api: Login.Api, val view: Login.View) {

    private var subscription: Subscription? = null

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
        subscription = api.login(login, password)
                .doOnSubscribe { view.showLoader() }
                .doOnUnsubscribe { view.hideLoader() }
                .subscribe({
                    view.openNextScreen()
                }, {
                    view.showLoginFailedError()
                })
    }

    fun onDestroy() {
        subscription?.unsubscribe()
    }

    companion object {
        private val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+").toRegex()
    }
}
