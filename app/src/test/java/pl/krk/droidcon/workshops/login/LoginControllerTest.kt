package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import rx.Observable

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Before
    fun setUp() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
    }

    @Test
    fun shouldShowErrorWhenLoginIsEmpty() {
        controller.onLogin("", "password")
        verify(view, times(1)).showEmptyCredentialError()
    }

    @Test
    fun shouldNotCallApiIfLoginIsEmpty() {
        controller.onLogin("", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenPasswordIsEmpty() {
        controller.onLogin("login", "")
        verify(view, times(1)).showEmptyCredentialError()
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        controller.onLogin("login", "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        whenever(api.login("login", "password")).thenReturn(Observable.error(RuntimeException()))
        controller.onLogin("login", "password")
        verify(view, times(1)).showLoginFailedError()
    }

    @Test
    fun shouldOpenNextScreenIfApiCallSucceed() {
        whenever(api.login("login123", "password123")).thenReturn(Observable.just(Unit))
        controller.onLogin("login123", "password123")
        verify(view, times(1)).openNextScreen()
    }

}

class LoginController(val api: Login.Api, val view: Login.View) {
    fun onLogin(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty()) {
            view.showEmptyCredentialError()
        } else {
            api.login(login, password).subscribe({
                view.openNextScreen()
            }, {
                view.showLoginFailedError()
            })
        }
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String): Observable<Unit>
    }

    interface View {
        fun showEmptyCredentialError()
        fun showLoginFailedError()
        fun openNextScreen()
    }
}
