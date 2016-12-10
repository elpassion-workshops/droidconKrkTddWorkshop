package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

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
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if ((password.length >= 3) and email.isNotEmpty() and password.isNotEmpty()) {
            api.login(email, password)
        }
    }
}

interface Login {
    interface Api {
        fun login(s: String, password: String)
    }
}
