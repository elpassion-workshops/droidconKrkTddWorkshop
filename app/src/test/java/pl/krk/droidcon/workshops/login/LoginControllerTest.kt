package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(api).login("email@test.pl", "mypass")
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(api).login("email@test.pl", "mypass")
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    private fun login(login: String = "email@test.pl", password: String = "mypass") {
        controller.onLogin(login, password)
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(login = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(api).login("email@test.pl", "mypass")
    }

    @Test
    fun shouldNotCallLoginWithEmptyPassword() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallLoginIfLoginIsCorrectAndPasswordIsEmpty() {
        login(login = "123", password = "")
        verify(api, never()).login(any(), any())

    }

    @Test
    fun shouldNotCallLoginIfPasswordIsNotHaveValidFormat() {
        login("name@test.pl", password = "12")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenLoginFails() {
        login(login = "")
        verify(view).showError(any())
    }

    @Test
    fun shouldNotShowErrorMessageOnSuccessfulLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
        login()
        verify(view, never()).showError(any())
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldShowErrorMessageOnFailedLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        login()
        verify(view).showLoadProgress()
        verify(view).showError(any())
        verify(view).hideLoadProgress()
    }

}
