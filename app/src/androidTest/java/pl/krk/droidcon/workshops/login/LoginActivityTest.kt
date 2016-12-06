package pl.krk.droidcon.workshops.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
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
    val rule = ActivityTestRule(LoginActivity::class.java)

    @Rule
    @JvmField
    val intentsRule = InitIntentsRule()

    @Before
    fun setUp() {
        Login.ApiProvider.override = { loginApi }
    }

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

    private fun login() {
        onId(R.id.loginLoginInput).typeText("email@test.pl")
        onId(R.id.loginPasswordInput).typeText("password")
        onId(R.id.loginLoginButton).click()
    }
}

