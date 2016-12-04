package pl.krk.droidcon.workshops.login

import rx.Observable

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<Unit>
    }

    interface View {
        fun showEmptyCredentialError()
        fun showLoginFailedError()
        fun openNextScreen()
        fun showLoader()
        fun hideLoader()
    }
}