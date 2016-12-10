package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.functions.Action1

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Before
    fun setup() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(createUserInfo()))
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        login("email@test.pl")
        verify(api).login(eq("email@test.pl"), any())
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        login("email2@test.pl")
        verify(api).login(eq("email2@test.pl"), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
       login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidePassword() {
        login( password = "password")
        verify(api).login(email = any(), password = eq("password" ))
    }

    @Test
    fun shouldReallyCallApiWithPassword() {
        login(password = "other-password")
        verify(api).login(email = any(), password = eq("other-password"))
    }

    @Test
    fun shouldNotCallApiWithEmptyPassword() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorMessageWhenEmailEmpty() {
        login(email = "")
        verify(view).showEmptyCredentialsError()
    }

    @Test
    fun shouldShowErrorMessageWhenPasswordIsEmpty() {
        login( password = "")
        verify(view).showEmptyCredentialsError()
    }

    @Test
    fun shouldNotCallApiWhenEmailIsIncorrect() {
        login(email = "wrong-email.test.pl")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldOpenNextScreenIfApiCallIsSuccessful() {
        whenever(api.login("email@test.pl","passw")).thenReturn(Observable.just(createUserInfo()))
        login("email@test.pl","passw")
        verify(view).openNextScreen()
    }

    @Test
    fun shouldShowErrorIfApiCallIsNotSuccessful() {
        whenever(api.login("email2@test.pl","passw2")).thenReturn(Observable.error(Exception("Login failed")))
        login("email2@test.pl","passw2")
        verify(view).showLoginFailed()
    }

    @Test
    fun shouldNotShowLoginFailedOnApiCAllSuccess() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(createUserInfo()))
        login()
        verify(view, never()).showLoginFailed()
    }

    @Test
    fun shouldShowLoaderOnLoginCall() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun shouldHideLoaderAfterLoginCall() {
        login()
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderOnNeverEndingCall() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        login()
        verify(view, never()).hideLoader()
    }

    private fun login(email: String = "asd@test.pl", password: String = "password") {
        controller.onLogin(email = email, password = password)
    }

    private fun createUserInfo(token : String = "some_token") : UserInfo {
        return UserInfo(token)
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (email.matches(EMAIL_REGEX)) {
                view.showLoader()
                api.login(email, password).subscribe({
                    view.openNextScreen()
                }, {view.showLoginFailed()
                }, {view.hideLoader()})

            }
        }
        else {
            view.showEmptyCredentialsError()
        }
    }

    companion object {
        private  val EMAIL_REGEX = ".*@.*".toRegex()
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String) : Observable<UserInfo>
    }

    interface View {
        fun showEmptyCredentialsError()
        fun openNextScreen()
        fun showLoginFailed()
        fun showLoader()
        fun hideLoader()
    }
}

class UserInfo (private val token : String)