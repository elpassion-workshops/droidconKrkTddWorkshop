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
        controller.onLogin("email@test.pl")
        verify(api).login("email@test.pl")
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        controller.onLogin("email2@test.pl")
        verify(api).login("email2@test.pl")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "")
        verify(api, never()).login(any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveAtChar() {
        controller.onLogin("emailtest.pl")
        verify(api, never()).login(any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDot() {
        controller.onLogin("email@testpl")
        verify(api, never()).login(any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDotAfterAtSign() {
        controller.onLogin("email.pl@test")
        verify(api, never()).login(any())
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String) {
        if (isEmailValid(email)) {
            api.login(email)
        }
    }

    private fun isEmailValid(email: String) : Boolean {
        return email.matches(Regex(".*@.*\\..*"))
    }
}

interface Login {
    interface Api {
        fun login(s: String)
    }
}
