package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    @Test
    fun shouldCallApiOnLogin() {
        val api = mock<Login.Api>()
        LoginController(api).onLogin()
        verify(api).login()
    }
}

class LoginController(private val api: Login.Api) {
    fun onLogin() {
        api.login()
    }
}

interface Login {
    interface Api {
        fun login()
    }
}
