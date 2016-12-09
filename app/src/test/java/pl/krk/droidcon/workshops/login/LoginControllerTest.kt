package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class LoginControllerTest {

    private val view = mock<Login.View>()
    private val api = mock<Login.Api>()
    private val facebookLoginCreator = mock<Login.FacebookLoginCreator>()
    private val loginController = LoginController(view, facebookLoginCreator, api)

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
    fun shouldSetupFacebookButtonProviderWithCallbacks() {
        val facebookButtonProvider = mock<Login.FacebookButtonProvider>()
        whenever(facebookLoginCreator.create(loginController)).thenReturn(facebookButtonProvider)
        loginController.onCreate()
        verify(view, times(1)).addFacebookButton(facebookButtonProvider)
    }

}