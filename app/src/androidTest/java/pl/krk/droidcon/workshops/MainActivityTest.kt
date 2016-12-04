package pl.krk.droidcon.workshops

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldStartActivity() {
    }
}
