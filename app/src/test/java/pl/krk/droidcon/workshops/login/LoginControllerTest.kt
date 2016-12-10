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
    fun shouldCallApiWithProvidedCredentials() {
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

    private fun doLogin(email: String = "email@test.pl", password : String = "password") {
        controller.onLogin(email, password)
    }

    private fun verifyLoginNotCalled() {
        verify(api, never()).login(any(), any())
    }
}

class LoginController(private val api: Login.Api) {

    private val EMAIL_PATTERN = ".+@.+".toRegex()

    fun onLogin(email: String, password: String) {
        if (password.isEmpty()) {
            return
        }
        if (email.isEmpty()) {
            return
        }
        if (!email.matches(EMAIL_PATTERN)) {
            return
        }
        api.login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}
