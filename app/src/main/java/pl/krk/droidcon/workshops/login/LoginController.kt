package pl.krk.droidcon.workshops.login

import rx.Subscription
import rx.subscriptions.CompositeSubscription

class LoginController(private val api: Login.Api, private val storage : Login.Storage,
                      private val view: Login.View) {

    private fun credentialsAreValid(email: String, password: String): Boolean =
            (password.length >= 3) and email.isNotEmpty() and password.isNotEmpty()

    private val compositeSubscription = CompositeSubscription()

    fun onLogin(email: String, password: String) {
        if (credentialsAreValid(email, password)) {
            api
                    .login(email, password)
                    .doOnSubscribe {
                        view.hideError()
                        view.showLoadProgress()
                    }
                    .doOnUnsubscribe { view.hideLoadProgress() }
                    .subscribe(
                            { user ->
                                storage.setUser(user)
                                view.showMainScreen() },
                            { throwable ->
                                view.showError("API login failed")
                            }
                    )
                    .save()
        } else {
            view.showError("Invalid login or password!")
        }
    }

    fun onDestroy() {
        compositeSubscription.clear()
    }

    private fun Subscription.save() {
        compositeSubscription.add(this)
    }
}

