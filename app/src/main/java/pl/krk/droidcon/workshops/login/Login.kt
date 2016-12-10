package pl.krk.droidcon.workshops.login

import rx.Observable

interface Login {
    interface Api {
        fun login(login: String, password: String) : Observable<User>
    }

    interface View {
        fun showError(message: String)
        fun hideError()
        fun showLoadProgress()
        fun hideLoadProgress()
        fun showMainScreen()
    }

    interface Storage {
        fun setUser(user: User)

    }
}