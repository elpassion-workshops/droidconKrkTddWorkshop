package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    val view = mock<Login.View>()
    val fbButtonProvider = mock<Login.FacebookButtonProvider>()
    val loginController = LoginController(view, fbButtonProvider)

    @Test
    fun shouldPassFacebookButtonProviderToViewOnCreate() {
        loginController.onCreate()

        verify(view).addFacebookButton(fbButtonProvider)
    }
}

class LoginController(private val view: Login.View,
                      private val fbButtonProvider: Login.FacebookButtonProvider) {

    fun onCreate() {
        view.addFacebookButton(fbButtonProvider)
    }

}
interface Login {

    interface FacebookButtonProvider

    interface View {
        fun addFacebookButton(fbButtonProvider: FacebookButtonProvider)
    }
}