package pl.krk.droidcon.workshops.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R
import rx.Observable

class LoginActivityTest {

    val api = mock<Login.Api>()

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Test
    fun shouldEmailHintBeVisible() {
        startActivity()
        onText(R.string.loginEmailHeader).isDisplayed()
    }

    @Test
    fun shouldHaveTypedEmailInTheInput() {
        startActivity()
        onId(R.id.loginEmailInput)
                .isDisplayed()
                .typeText("email@test.pl")
                .hasText("email@test.pl")
    }

    @Test
    fun shouldPasswordHintBeVisible() {
        startActivity()
        onText(R.string.loginPasswordHeader).isDisplayed()
    }

    @Test
    fun shouldHaveTypedPasswordInTheInput() {
        startActivity()
        onId(R.id.loginPasswordInput)
                .isDisplayed()
                .typeText("abcdef19")
                .hasText("abcdef19")
    }

    @Test
    fun shouldTypedPasswordBeStarred() {
        startActivity()
        onId(R.id.loginPasswordInput)
                .typeText("abcdef19")
                .check(matches(withInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun shouldLoginButtonBeEnabled() {
        startActivity()
        onId(R.id.loginButton).isEnabled()
    }

    @Test
    fun shouldShowErrorWhenLoginFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        startActivity()
        onId(R.id.loginButton).click()
        onId(R.id.loginError).isDisplayed()
    }

    private fun startActivity() {
        rule.launchActivity(null)
    }
}
