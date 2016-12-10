package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login(eq("email@test.pl"), any())
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(eq("email2@test.pl"), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(any(), eq("password"))
    }

    @Test
    fun shouldReallyCallApiWithProvidedPassword() {
        controller.onLogin("any login", "password2")
        verify(api).login(any(), eq("password2"))
    }

}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {

        if (email.isNotEmpty()) {
            api.login(email, password)
        }
    }


}

interface Login {
    interface Api {
        fun login(s: String, password: String)
    }
}
