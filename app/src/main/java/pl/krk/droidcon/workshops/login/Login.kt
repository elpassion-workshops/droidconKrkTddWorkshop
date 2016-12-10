package pl.krk.droidcon.workshops.login

import rx.Observable

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<User>
    }

    interface View {
        fun showError()
        fun gotoHomeScreen()
        fun showLoader()
        fun hideLoader()
    }
}