package pl.krk.droidcon.workshops.login

import pl.krk.droidcon.workshops.R
import rx.Observable
import rx.Scheduler
import rx.Subscription

class LoginController(private val api: Login.Api,
                      private val view: Login.View,
                      private val userStorage: Login.UserStorage,
                      private val observeOnScheduler: Scheduler,
                      private val subscribeOnScheduler: Scheduler) {

    private var subscription: Subscription? = null

    fun onLogin(email: String, password: String) {
        if (email.isEmpty()) {
            view.showError(R.string.emailErrorText)
            return
        }
        if (password.isNotEmpty()) {
            subscription = api.login(email, password)
                    .handleLoader(view)
                    .subscribeOn(subscribeOnScheduler)
                    .observeOn(observeOnScheduler)
                    .doOnNext { userStorage.saveUserData(it) }
                    .subscribe({
                        view.gotoHomeScreen()
                    }, {
                        view.showError()
                    })
        } else {
            view.showError()
        }
    }

    fun onDestroy() {
        subscription?.unsubscribe()
    }

    private fun <T> Observable<T>.handleLoader(view: Login.View) = this
            .doOnSubscribe { view.showLoader() }
            .doOnUnsubscribe { view.hideLoader() }
}