package pl.krk.droidcon.workshops.login

import android.view.ViewGroup
import pl.krk.droidcon.workshops.Provider
import rx.Observable

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<User>

        fun loginWithFacebook(token: String): Observable<User>
    }

    interface View {
        fun showEmptyCredentialError()
        fun showLoginFailedError()
        fun openNextScreen()
        fun showLoader()
        fun hideLoader()
        fun showInvalidEmailError()
        fun setupFacebookButton(createButtonFunction: (ViewGroup) -> android.view.View)
    }

    object ApiProvider : Provider<Api>({
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    })
}