package pl.krk.droidcon.workshops.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.click
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onId
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.krk.droidcon.workshops.R

class LoginActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Before
    fun setUp() {
        Login.LoginApiProvider.override = mock()
    }

    @Test
    fun shouldShowFacebookButton() {
        rule.launchActivity(null)
        onId(R.id.facebookButton).isDisplayed()
    }

    @Test
    fun shouldCallApiAfterFacebookLoginSuccess() {
        val api = mock<Login.Api>()
        Login.LoginApiProvider.override = api
        rule.launchActivity(null)
        onId(R.id.facebookButton).click()

        verify(api, times(1)).loginWithFbToken(any())
    }
}
