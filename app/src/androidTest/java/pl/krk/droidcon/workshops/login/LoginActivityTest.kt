package pl.krk.droidcon.workshops.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.hasText
import com.elpassion.android.commons.espresso.onId
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
}