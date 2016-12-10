package pl.krk.droidcon.workshops.login

import rx.Observable
import rx.Scheduler
import rx.Subscription

class LoginController(private val api: Login.Api, val view: Login.View, val userStorage: UserStorage, val scheduler: Scheduler,
                      val subscribeOnScheduler: Scheduler) {

    private var subscription: Subscription? = null

    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            subscription = api.login(email, password)
                    .handleLoader(view)
                    .subscribeOn(subscribeOnScheduler)
                    .observeOn(scheduler)
                    .subscribe({
                        userStorage.saveUserData(it)
                        view.gotoHomeScreen()
                    }, {
                        view.showError()
                    })
        }
    }

    fun onDestroy() {
        subscription?.unsubscribe()
    }

    private fun <T> Observable<T>.handleLoader(view: Login.View) =
            doOnSubscribe { view.showLoader() }
                    .doOnUnsubscribe { view.hideLoader() }
}