package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable
import rx.Subscription

class LoginControllerTest {

    private val api = mock<Login.Api> {
        on { login(any(), any()) } doReturn Observable.never()
    }
    val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login(eq("email@test.pl"), any())
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(eq("email2@test.pl"), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login(any(), eq("password"))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException("login failed")))
        login()
        verify(view).showError()
    }

    @Test
    fun shouldNotShowErrorWhenApiCallSuccess() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(view, never()).showError()
    }

    @Test
    fun shouldMoveToHomeScreenWhenApiCallSucceed() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(view).gotoHomeScreen()
    }

    @Test
    fun shouldShowLoaderAfterClickingLoginButton() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun shouldNotShowLoaderWhenEmailIsEmpty() {
        login("", "some-password")
        verify(view, never()).showLoader()
    }

    @Test
    fun shouldHideLoaderAfterApiCallCompletes() {
        whenever(api.login(any(), any())) doReturn Observable.just(Unit)
        login("email@email.pl", "some-password")
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderUntilApiCallCompletes() {
        whenever(api.login(any(), any())) doReturn Observable.never()
        login("email@email.pl", "some-password")
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldHideLoaderWhenControllerCallsOnDestroy() {
        login("qwerty", "asdfgh")
        controller.onDestroy()
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderOnDestroyWhenNotShownBefore() {
        controller.onDestroy()
        verify(view, never()).hideLoader()
    }

    private fun login(email: String = "email@test.pl", password: String = "some-password") {
        controller.onLogin(email, password)
    }
}

class LoginController(private val api: Login.Api, val view: Login.View) {

    private var subscription: Subscription? = null

    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            subscription = api.login(email, password)
                    .subscribe({ view.gotoHomeScreen() }, { view.showError() }, { view.hideLoader() })
        }
    }

    fun onDestroy() {
        if (!(subscription?.isUnsubscribed ?: true)) {
            view.hideLoader()
        }
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<Unit>
    }

    interface View {
        fun showError()
        fun gotoHomeScreen()
        fun showLoader()
        fun hideLoader()
    }
}
