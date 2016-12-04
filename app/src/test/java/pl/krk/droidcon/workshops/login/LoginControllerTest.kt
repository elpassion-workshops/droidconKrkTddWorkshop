package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import org.junit.Test
import org.mockito.Mockito.verify

class LoginControllerTest {

    @Test
    fun shouldCallApiOnLoginWithCorrectLoginAndPassword() {
        val api = mock<Login.Api>()
        LoginController(api).onLogin("login", "password")
        verify(api, times(1)).login("login", "password")
    }

    @Test
    fun shouldCallApiOnLoginWithReallyCorrectLoginAndPassword() {
        val api = mock<Login.Api>()
        LoginController(api).onLogin("login123", "password123")
        verify(api, times(1)).login("login123", "password123")
    }

}

class LoginController(val api: Login.Api) {
    fun onLogin(login: String, password: String) {
        api.login(login, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}
