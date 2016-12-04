package pl.krk.droidcon.workshops.login

import rx.Subscription

class LoginController(val api: Login.Api, val view: Login.View) {

    private var subscription: Subscription? = null

    fun onLogin(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) {
            view.showEmptyCredentialError()
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
}