package pl.krk.droidcon.workshops.login

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.view.ViewGroup
import android.widget.Button
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.InitIntentsRule
import pl.krk.droidcon.workshops.R
import pl.krk.droidcon.workshops.next.NextScreenActivity
import rx.Observable.*

class LoginActivityTest() {

    private val loginApi = mock<Login.Api> { on { login(any(), any()) } doReturn never() }

    @Rule
    @JvmField
    val rule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            stubFacebookButton()
            Login.ApiProvider.override = { loginApi }
        }
    }

    @Rule
    @JvmField
    val intentsRule = InitIntentsRule()

    @Test
    fun shouldHaveLoginHeader() {
        onId(R.id.loginLoginHeader).hasText(R.string.loginLoginHeader)
    }

    @Test
    fun shouldHaveTypedLogin() {
        onId(R.id.loginLoginInput).typeText("login").hasText("login")
    }

    @Test
    fun shouldHavePasswordHeader() {
        onId(R.id.loginPasswordHeader).hasText(R.string.loginPasswordHeader)
    }

    @Test
    fun shouldHaveLoginButton() {
        onId(R.id.loginLoginButton).hasText(R.string.loginLoginButton)
    }

    @Test
    fun shouldHavePasswordInput() {
        onId(R.id.loginPasswordInput).typeText("password").hasText("password")
    }

    @Test
    fun shouldShowErrorWhenTryToLoginWithEmptyLogin() {
        onId(R.id.loginPasswordInput).typeText("password")
        onId(R.id.loginLoginButton).click()
        onText(R.string.loginEmptyCredentialError).isDisplayed()
    }

    @Test
    fun shouldShowErrorWhenTryToLoginWithEmptyPassword() {
        onId(R.id.loginLoginInput).typeText("login")
        onId(R.id.loginLoginButton).click()
        onText(R.string.loginEmptyCredentialError).isDisplayed()
    }

    @Test
    fun shouldShowInvalidEmailErrorWhenEmailIsInvalid() {
        onId(R.id.loginLoginInput).typeText("login")
        onId(R.id.loginPasswordInput).typeText("password")
        onId(R.id.loginLoginButton).click()
        onText(R.string.invalidEmailError).isDisplayed()
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        whenever(loginApi.login(any(), any())) doReturn error(RuntimeException())
        login()
        onText(R.string.loginCallFailedError).isDisplayed()
    }

    @Test
    fun shouldOpenNextScreenWhenCallToApiSucceed() {
        whenever(loginApi.login(any(), any())) doReturn just(User(1))
        login()
        checkIntent(NextScreenActivity::class.java)
    }

    @Test
    fun shouldOpenNextScreenAfterLoginWithFacebookSucceed() {
        Thread.sleep(1000)
        loginWithFacebook()
        checkIntent(NextScreenActivity::class.java)
    }

    private fun loginWithFacebook() {
        onView(ViewMatchers.withId(R.id.facebookButton)).click()
    }

    private fun login() {
        onId(R.id.loginLoginInput).typeText("email@test.pl")
        onId(R.id.loginPasswordInput).typeText("password")
        onId(R.id.loginLoginButton).click()
    }

    private fun stubFacebookButton() {
        FacebookLoginButtonProvider.overided = object : FacebookLoginButtonProvider {
            override fun addToContainer(viewGroup: ViewGroup, onSuccess: () -> Unit, onError: () -> Unit) {
                val button = Button(viewGroup.context).apply {
                    id = R.id.facebookButton
                    text = "Login with facebook"
                    setOnClickListener({
                        onSuccess()
                    })
                }
                viewGroup.addView(button)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            }
        }
    }
}