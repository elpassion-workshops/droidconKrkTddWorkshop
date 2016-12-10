package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

    @Test
    fun shouldCallApiWithProvidedEmail() {
        login("email@test.pl")
        verify(api).login(eq("email@test.pl"), any())
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        login("email2@test.pl")
        verify(api).login(eq("email2@test.pl"), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
       login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidePassword() {
        login( password = "password")
        verify(api).login(email = any(), password = eq("password" ))
    }

    @Test
    fun shouldReallyCallApiWithPassword() {
        login(password = "other-password")
        verify(api).login(email = any(), password = eq("other-password"))
    }

    @Test
    fun shouldNotCallApiWithEmptyPassword() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorMessageWhenEmailEmpty() {
        login(email = "")

        verify(view).showEmptyCredentialsError()
    }

    @Test
    fun shouldShowErrorMessageWhenPasswordIsEmpty() {
        login( password = "")

        verify(view).showEmptyCredentialsError()
    }

    private fun login(email: String = "asd@test.pl", password: String = "password") {
        controller.onLogin(email = email, password = password)
    }

}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
        }
        else {
            view.showEmptyCredentialsError()
        }
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }

    interface View {
        fun showEmptyCredentialsError()

    }
}
