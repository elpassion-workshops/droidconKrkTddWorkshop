package pl.krk.droidcon.workshops.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import com.elpassion.android.commons.espresso.*
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R

class LoginActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun shouldEmailHintBeVisible() {
        onText(R.string.loginEmailHeader).isDisplayed()

        // version without android.commons below
        Espresso.onView(ViewMatchers.withText(R.string.loginEmailHeader))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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
                .typeText("pass")
                .hasText("pass")
    }

    @Test
    fun shouldPasswordInputBePasswordType() {
        onId(R.id.loginPasswordInput)
                .check(ViewAssertions.matches(ViewMatchers.withInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)))
    }

    @Test
    fun shouldLoginButtonBeVisible() {
        onId(R.id.loginLoginButton)
                .isDisplayed()
    }

    @Test
    fun shouldLoginButtonHasLoginText() {
        onId(R.id.loginLoginButton)
                .hasText(R.string.loginLoginText)
    }
}
