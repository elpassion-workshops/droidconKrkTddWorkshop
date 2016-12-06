package pl.krk.droidcon.workshops.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R

class LoginActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java)

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
}