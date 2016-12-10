package pl.krk.droidcon.workshops.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
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
    fun shouldEmailHintBeVisible() {
        onText(R.string.loginEmailHeader).isDisplayed()

        // version without android.commons below
        Espresso.onView(ViewMatchers.withText(R.string.loginEmailHeader))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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

    private fun checkTypedTextIsDisplayedInInput(text: String, viewId: Int) {
        onId(viewId)
                .isDisplayed()
                .typeText(text)
                .hasText(text)
    }
}
