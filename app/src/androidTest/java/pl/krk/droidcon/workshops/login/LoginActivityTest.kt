package pl.krk.droidcon.workshops.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withInputType
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
                .check(matches(ViewMatchers.isDisplayed()))
    }
    
    @Test
    fun shouldHaveTypedEmailInTheInput() {
        checkTypedTextIsDisplayedInInput("email@test.pl", R.id.loginEmailInput)
    }

    @Test
    fun shouldHaveTypedPasswordInTheInput() {
        checkTypedTextIsDisplayedInInput("secret", R.id.loginPasswordInput)
    }

    @Test
    fun shouldPasswordHintBeVisible() {
        onText(R.string.loginPasswordHeader).isDisplayed()
    }

    @Test
    fun shouldTextInPasswordInputBeMasked() {
        onId(R.id.loginPasswordInput)
                .typeText("secret")
                .check(matches(withInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
    }

    private fun checkTypedTextIsDisplayedInInput(text: String, viewId: Int) {
        onId(viewId)
                .isDisplayed()
                .typeText(text)
                .hasText(text)
    }
}
