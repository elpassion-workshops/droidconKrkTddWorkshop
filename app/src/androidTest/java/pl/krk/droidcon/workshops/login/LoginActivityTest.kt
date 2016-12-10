package pl.krk.droidcon.workshops.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R
import rx.Observable

class LoginActivityTest {

    val api = mock<Login.Api>()

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        LoginApiProvider.override = api
    }

    @Test
    fun shouldEmailHintBeVisible() {
        onText(R.string.loginEmailHeader).isDisplayed()
    }

    @Test
    fun shouldHaveTypedEmailInTheInput() {
        onId(R.id.loginEmailInput)
                .isDisplayed()
                .typeText("email@test.pl")
                .hasText("email@test.pl")
    }

    @Test
    fun shouldPasswordHintBeVisible() {
        onText(R.string.loginPasswordHeader).isDisplayed()
    }

    @Test
    fun shouldHaveTypedPasswordInTheInput() {
        onId(R.id.loginPasswordInput)
                .isDisplayed()
                .typeText("abcdef19")
                .hasText("abcdef19")
    }

    @Test
    fun shouldTypedPasswordBeStarred() {
        onId(R.id.loginPasswordInput)
                .typeText("abcdef19")
                .check(matches(withInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun shouldLoginButtonBeEnabled() {
        onId(R.id.loginButton).isEnabled()
    }

    @Test
    fun shouldShowErrorWhenLoginFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        onId(R.id.loginButton).click()
        onId(R.id.loginError).isDisplayed()
    }

    @Test
    fun shouldNotShowErrorWhenLoginPass() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(User("1")))
        onId(R.id.loginEmailInput)
                .typeText("email@test.pl")
        Espresso.closeSoftKeyboard()
        onId(R.id.loginPasswordInput)
                .typeText("password")
        Espresso.closeSoftKeyboard()
        onId(R.id.loginButton).click()
        onId(R.id.loginError).isNotDisplayed()
    }

    @Test
    fun shouldErrorHaveText() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        onId(R.id.loginError).hasText(R.string.loginErrorText)
    }

    @Test
    fun shouldErrorDisplayLackOfEmailMessage() {
        onId(R.id.loginButton).click()
        onId(R.id.loginError).hasText(R.string.loginCredentialEmptyError)

    }

    @Test
    fun shouldShowProgressBarWhenLoginStarted() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        onId(R.id.loginButton).click()
        onId(R.id.loginProgressBar).isDisplayed()
    }
}
