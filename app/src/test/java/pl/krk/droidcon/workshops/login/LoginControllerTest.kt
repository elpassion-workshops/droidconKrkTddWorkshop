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
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login("email@test.pl", "password")
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty()) {
            api.login(email, "password")
        }
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}
