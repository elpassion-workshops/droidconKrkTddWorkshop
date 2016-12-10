package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
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
        controller.onLogin("email2@test.pl","password")
        verify(api).login("email2@test.pl", "password")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password =  "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidePassword() {
        controller.onLogin(email = "email@test.pl", password = "password")
        verify(api).login(email = any(), password = eq("password" ))
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
        fun login(email: String, password: String)
    }
}
