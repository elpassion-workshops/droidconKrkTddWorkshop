package pl.krk.droidcon.workshops.login

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import com.elpassion.android.commons.espresso.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
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
                .typeText("any password")
                .hasText("any password")
    }

    @Test
    fun shouldPasswordIsPasswordType() {
        onId(R.id.loginPasswordInput)
                .check(ViewAssertions.matches(ViewMatchers.withInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun shouldLoginWhenClickLoginButton() {
        val api = mock<Login.Api>()

        onId(R.id.loginEmailInput).typeText("any login")
        onId(R.id.loginPasswordInput).typeText("any password")
        onId(R.id.loginButton).click()

        verify(api).login(any(), any())
    }
}