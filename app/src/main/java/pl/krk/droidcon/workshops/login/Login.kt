package pl.krk.droidcon.workshops.login

import pl.krk.droidcon.workshops.Provider
import rx.Observable

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<User>
    }

    interface View {
        fun showEmptyCredentialError()
        fun showLoginFailedError()
        fun openNextScreen()
        fun showLoader()
        fun hideLoader()
        fun showInvalidEmailError()
        fun setupFacebookButton()
    }

    object ApiProvider : Provider<Api>({
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    })
}