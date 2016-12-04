package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import org.junit.Test
import org.mockito.Mockito.verify

class LoginControllerTest {

    @Test
    fun shouldCallApiOnLogin() {
        val api = mock<Login.Api>()
        LoginController(api).onLogin()
        verify(api, times(1)).login()
    }
}

class LoginController(val api: Login.Api) {
    fun onLogin() {
        api.login()
    }
}

interface Login {
    interface Api {
        fun login()
    }
}
