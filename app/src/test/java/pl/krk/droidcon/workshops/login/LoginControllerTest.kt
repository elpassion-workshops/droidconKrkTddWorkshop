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
        controller.onLogin("email@test.pl", "password")
        verify(api).login("email@test.pl", "password")
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login("email2@test.pl", "password")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveAtChar() {
        controller.onLogin("emailtest.pl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDot() {
        controller.onLogin("email@testpl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDotAfterAtSign() {
        controller.onLogin("email.pl@test", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithPassword() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login("email@test.pl", "password")
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if (isEmailValid(email)) {
            api.login(email, "password")
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
