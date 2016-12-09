package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    val view = mock<Login.View>()
    val api = mock<Login.Api>()
    val fbButtonProvider = mock<Login.FacebookButtonProvider>()
    val loginController = LoginController(view, fbButtonProvider, api)

    @Test
    fun shouldPassFacebookButtonProviderToViewOnCreate() {
        loginController.onCreate()

        verify(view).addFacebookButton(fbButtonProvider)
    }

    @Test
    fun shouldCallApiWithFbTokenAfterLoginSucceed() {
        loginController.onFacebookLoginSuccess("token")
        verify(api).loginWithFbToken("token")
    }
}

class LoginController(private val view: Login.View,
                      private val fbButtonProvider: Login.FacebookButtonProvider,
                      private val api: Login.Api) {

    fun onCreate() {
        view.addFacebookButton(fbButtonProvider)
    }

    fun onFacebookLoginSuccess(token: String) {
        api.loginWithFbToken(token)
    }

}
interface Login {

    interface FacebookButtonProvider

    interface View {
        fun addFacebookButton(fbButtonProvider: FacebookButtonProvider)
    }

    interface Api {
        fun loginWithFbToken(token: String)
    }
}