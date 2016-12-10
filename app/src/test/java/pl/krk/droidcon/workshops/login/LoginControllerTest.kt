package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        login()
        verify(api).login("email@test.pl", "mypass")
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        login()
        verify(api).login("email@test.pl", "mypass")
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
    fun shouldNotShowErrorOnSuccessfulLogin() {
        login()
        verify(view, never()).showError(any())
    }

    @Test
    fun shouldHideErrorMessageOnLogin() {
        view.showError("error message")
        login()
        verify(view).hideError()
    }
}
