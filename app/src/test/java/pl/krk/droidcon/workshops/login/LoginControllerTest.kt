package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable

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
        controller.onLogin(email = "email@test.pl", password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException("login failed")))
        controller.onLogin("email@test.pl", "some-password")
        verify(view).showError()
    }

    @Test
    fun shouldNotShowErrorWhenApiCallSuccess() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        controller.onLogin("email@test.pl", "some-password")
        verify(view, never()).showError()
    }

    @Test
    fun shouldMoveToHomeScreenWhenApiCallSucceed() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        controller.onLogin("email@test.pl", "some-password")
        verify(view).gotoHomeScreen()
    }
}

class LoginController(private val api: Login.Api, val view: Login.View) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
                    .subscribe({view.gotoHomeScreen()}, { view.showError() })
        }
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<Unit>
    }

    interface View {
        fun showError() {

        }

        fun gotoHomeScreen() {

        }

    }
}
