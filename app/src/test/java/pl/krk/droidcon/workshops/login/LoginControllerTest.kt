package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
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
}

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String) {
        api.login(email)
    }
}

interface Login {
    interface Api {
        fun login(s: String)
    }
}
