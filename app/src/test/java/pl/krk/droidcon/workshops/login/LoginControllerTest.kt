package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiWithProvidedCredentials() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        doLogin("email@test.pl", "password2")
        verify(api).login("email@test.pl", "password2")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        doLogin(email = "")
        verifyLoginNotCalled()
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesNotContainAt() {
        doLogin(email = "emailtest.pl")
        verifyLoginNotCalled()
    }

    @Test
    fun shouldNotCallApiWhenEmailContainsAtOnly() {
        doLogin(email = "@")
        verifyLoginNotCalled()
    }

    @Test
    fun shouldNotCallApiWhenPasswordEmpty() {
        doLogin(password = "")
        verifyLoginNotCalled()
    }

    @Test
    fun shouldOpenNewScreenWhenLoginCalled() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        doLogin()
        verify(view).openNextScreen()
    }

    @Test
    fun shouldShowErrorWhenLoginFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        doLogin()
        verify(view).showError()
    }

    @Test
    fun shouldNotOpenNewScreenWhenLoginFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        doLogin()
        verify(view, never()).openNextScreen()
    }

    @Test
    fun shouldNotShowErrorWhenLoginSucceeds() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        doLogin()
        verify(view, never()).showError()
    }


    private fun doLogin(email: String = "email@test.pl", password : String = "password") {
        controller.onLogin(email, password)
    }

    private fun verifyLoginNotCalled() {
        verify(api, never()).login(any(), any())
    }
}

