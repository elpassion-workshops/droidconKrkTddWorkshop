package pl.krk.droidcon.workshops.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.hasText
import com.elpassion.android.commons.espresso.onId
import com.elpassion.android.commons.espresso.typeText
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
}