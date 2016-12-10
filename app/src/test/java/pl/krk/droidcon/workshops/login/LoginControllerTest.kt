package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Before
    fun setup() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
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
        whenever(api.login("email@test.pl","passw")).thenReturn(Observable.just(Unit))
        login("email@test.pl","passw")
        verify(view).openNextScreen()
    }

    private fun login(email: String = "asd@test.pl", password: String = "password") {
        controller.onLogin(email = email, password = password)
    }

}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (email.matches(EMAIL_REGEX)) {
                api.login(email, password).subscribe({
                    view.openNextScreen()
                })
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
        fun login(email: String, password: String) : Observable<Unit>
    }

    interface View {
        fun showEmptyCredentialsError()
        fun openNextScreen()
    }
}
