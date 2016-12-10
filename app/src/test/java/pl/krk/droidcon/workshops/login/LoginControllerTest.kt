package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

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

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        controller.onLogin(email = "any login", password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorMessageWhenApiFailed() {
        controller.onLogin("any login", "any password")

        com.nhaarman.mockito_kotlin.whenever(api.login(any(), any())).thenReturn(false)

        verify(view).displayErrorMessage()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun onLogin(email: String, password: String) {
        if (password.isEmpty() || email.isEmpty()) {
            return
        }

        val result = api.login(email, password)
        if (!result) {
            view.displayErrorMessage()
        }
    }
}


interface Login {
    interface Api {
        fun login(s: String, password: String): Boolean
    }

    interface View {
        fun displayErrorMessage()
    }
}
