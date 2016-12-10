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
        doLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenPasswordEmpty() {
        doLogin(password = "")
        verify(api, never()).login(any(), any())
    }

    private fun doLogin(email: String = "email@test.pl", password : String = "password") {
        controller.onLogin(email, password)
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
        }
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}
