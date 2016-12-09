package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class LoginControllerTest {

    val view = mock<Login.View>()
    val api = mock<Login.Api>()
    val facebookLoginCreator = mock<Login.FacebookLoginCreator>()
    val loginController = LoginController(view, facebookLoginCreator, api)

    @Test
    fun shouldCallApiWithFbTokenAfterLoginSucceed() {
        loginController.onFacebookLoginSuccess("token")
        verify(api).loginWithFbToken("token")
    }

    @Test
    fun shouldShowLoginFailsErrorWhenLoginWithFacebookFails() {
        loginController.onFacebookLoginError()
        verify(view).showLoginFailsError()
    }

    @Test
    fun shouldPassFacebookButtonProviderCreatedByFacebookLoginCreator() {
        val facebookButtonProvider = mock<Login.FacebookButtonProvider>()
        whenever(facebookLoginCreator.create()).thenReturn(facebookButtonProvider)
        loginController.onCreate()
        verify(view, times(1)).addFacebookButton(facebookButtonProvider)
    }
}

class LoginController(private val view: Login.View,
                      private val facebookLoginCreator: Login.FacebookLoginCreator,
                      private val api: Login.Api) {

    fun onCreate() {
        view.addFacebookButton(facebookLoginCreator.create())
    }

    fun onFacebookLoginSuccess(token: String) {
        api.loginWithFbToken(token)
    }

    fun onFacebookLoginError() {
        view.showLoginFailsError()
    }

}
interface Login {

    interface FacebookButtonProvider

    interface FacebookLoginCreator {
        fun  create(): FacebookButtonProvider
    }

    interface View {
        fun addFacebookButton(fbButtonProvider: FacebookButtonProvider)
        fun showLoginFailsError()
    }

    interface Api {
        fun loginWithFbToken(token: String)
    }
}