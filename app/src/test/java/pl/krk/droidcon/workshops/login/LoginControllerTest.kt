package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        login()
        verify(api).login("email@test.pl", "password")
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        login("email2@test.pl", "password")
        verify(api).login("email2@test.pl", "password")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveAtChar() {
        login("emailtest.pl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDot() {
        login("email@testpl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDotAfterAtSign() {
        login("email.pl@test", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithPassword() {
        login()
        verify(api).login("email@test.pl", "password")
    }

    @Test
    fun shouldReallyCallApiWithPassword() {
        login(password = "pass")
        verify(api).login(any(), eq("pass"))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    private fun login(email: String = "email@test.pl", password: String = "password") {
        controller.onLogin(email, password)
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if (isEmailValid(email) && password.isNotEmpty()) {
            api.login(email, password)
        }
    }

    private fun isEmailValid(email: String) : Boolean {
        return email.matches(Regex(".*@.*\\..*"))
    }
}

interface Login {
    interface Api {
        fun login(s: String, password: String)
    }
}
