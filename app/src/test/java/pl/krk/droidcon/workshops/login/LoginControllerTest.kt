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

}

class LoginController(val api: Login.Api) {
    fun onLogin(login: String, password: String) {
        api.login("login", "password")
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}
