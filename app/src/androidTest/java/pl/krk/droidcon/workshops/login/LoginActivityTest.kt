package pl.krk.droidcon.workshops.login

import android.content.Context
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.view.ViewGroup
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R

class LoginActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Before
    fun setUp() {
        FacebookLoginCreatorProvider.override = SuccessFacebookLoginCreator()
        Login.LoginApiProvider.override = mock()
    }

    @Test
    fun shouldShowFacebookButton() {
        rule.launchActivity(null)
        onId(R.id.facebookButton).isDisplayed()
    }

    @Test
    fun shouldCallApiAfterFacebookLoginSuccess() {
        val api = mock<Login.Api>()
        Login.LoginApiProvider.override = api
        rule.launchActivity(null)
        onId(R.id.facebookButton).click()

        verify(api, times(1)).loginWithFbToken(any())
    }

    @Test
    fun shouldShowErrorWhenFacebookLoginFails() {
        FacebookLoginCreatorProvider.override = ErrorFacebookLoginCreator()

        rule.launchActivity(null)
        onId(R.id.facebookButton).click()

        onId(R.id.loginErrorMessage)
                .isDisplayed()
                .hasText(R.string.facebook_login_error)
    }

    @Test
    fun shouldNotShowErrorOnStart() {
        rule.launchActivity(null)
        onId(R.id.loginErrorMessage).isNotDisplayed()
    }

    @Test
    fun shouldNotShowErrorWhenFacebookLoginSucceed() {
        FacebookLoginCreatorProvider.override = SuccessFacebookLoginCreator()

        rule.launchActivity(null)
        onId(R.id.facebookButton).click()

        onId(R.id.loginErrorMessage).isNotDisplayed()
    }
}

class SuccessFacebookLoginCreator : Login.FacebookLoginCreator {
    override fun create(callbacks: Login.FacebookLoginCallbacks): Login.FacebookButtonProvider {
        return object : Login.FacebookButtonProvider {
            override fun getButton(viewGroup: ViewGroup): View {
                return View(viewGroup.context).apply {
                    id = R.id.facebookButton
                    setOnClickListener {
                        callbacks.onFacebookLoginSuccess("token")
                    }
                }
            }
        }
    }
}
class ErrorFacebookLoginCreator : Login.FacebookLoginCreator {
    override fun create(callbacks: Login.FacebookLoginCallbacks): Login.FacebookButtonProvider {
        return object : Login.FacebookButtonProvider {
            override fun getButton(viewGroup: ViewGroup): View {
                return View(viewGroup.context).apply {
                    id = R.id.facebookButton
                    setOnClickListener {
                        callbacks.onFacebookLoginError()
                    }
                }
            }
        }
    }
}
