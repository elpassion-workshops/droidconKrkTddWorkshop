package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import org.junit.Test
import org.mockito.Mockito.verify

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiOnLoginWithCorrectLoginAndPassword() {
        controller.onLogin("login", "password")
        verify(api, times(1)).login("login", "password")
    }

    @Test
    fun shouldCallApiOnLoginWithReallyCorrectLoginAndPassword() {
        controller.onLogin("login123", "password123")
        verify(api, times(1)).login("login123", "password123")
    }

    @Test
    fun shouldShowErrorWhenLoginIsEmpty() {
        controller.onLogin("", "password")
        verify(view, times(1)).showLoginEmptyError()
    }

}

class LoginController(val api: Login.Api, val view: Login.View) {
    fun onLogin(login: String, password: String) {
        if (login.isEmpty()){
            view.showLoginEmptyError()
        }
        api.login(login, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }

    interface View {
        fun showLoginEmptyError()
    }
}
