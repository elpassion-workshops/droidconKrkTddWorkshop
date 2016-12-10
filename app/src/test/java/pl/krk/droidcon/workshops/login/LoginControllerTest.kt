package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable

class LoginControllerTest {

    private val ERR_CANNOT_LOGIN = "Cannot login"
    private val ERR_WRONG_EMAIL = "Wrong email"

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val emailChecker = mock<Login.EmailChecker>()
    private val controller = LoginController(api, view, emailChecker)

    @Before
    fun setUp() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        whenever(emailChecker.verifyEmail(any())).thenReturn(true)
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        controller.onLogin("email@test.pl", "password")
        verify(api).login(eq("email@test.pl"), any())
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(eq("email2@test.pl"), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        controller.onLogin(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(any(), eq("password"))
    }

    @Test
    fun shouldReallyCallApiWithProvidedPassword() {
        controller.onLogin("any login", "password2")
        verify(api).login(any(), eq("password2"))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        controller.onLogin(email = "any login", password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowLoginErrorMessageWhenLoginFailed() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))

        controller.onLogin("any login", "any password")

        verify(view).displayErrorMessage(ERR_CANNOT_LOGIN)
    }

    @Test
    fun shouldShowErrorMessageWhenApiSucceed() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))

        controller.onLogin("any login", "any password")

        verify(view).goToAnotherScreen()
    }

    @Test
    fun shouldShowProgressBarWhenLogin() {
        controller.onLogin("any login", "any password")

        verify(view).displayProgressBar()
    }

    @Test
    fun shouldHideProgressBarWhenLoginEnd() {
        whenever(api.login(any(), any())).thenReturn(Observable.empty())
        controller.onLogin("any login", "any password")

        verify(view).hideProgressBar()
    }

    @Test
    fun shouldShowProgressBefore() {
        controller.onLogin("email2@test.pl", "password")
        verify(api).login(any(), eq("password"))
    }

    @Test
    fun shouldShowProgressBeforeHideProgressWhenLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.empty())

        controller.onLogin("email2@test.pl", "password")

        val inOrder = inOrder(view, api)
        inOrder.verify(view).displayProgressBar()
        inOrder.verify(api).login(any(), any())
        inOrder.verify(view).hideProgressBar()
    }

    @Test
    fun shouldShowErrorMessageWhenEmailIsInvalid() {
        whenever(emailChecker.verifyEmail("email@com")).thenReturn(false)

        controller.onLogin("email@com", "password")

        verify(view).displayErrorMessage(ERR_WRONG_EMAIL)
    }

    @Test
    fun shouldNotTouchViewWhenEmailIsNotValid() {
        // given
        whenever(emailChecker.verifyEmail("email@com")).thenReturn(false)

        // when
        controller.onLogin("email@com", "password")
        verify(view).displayErrorMessage(any())

        // then
        verifyNoMoreInteractions(view)
    }

    @Test
    fun shouldNotTouchApiWhenEmailIsNotValid() {
        whenever(emailChecker.verifyEmail("email@com")).thenReturn(false)

        controller.onLogin("email@com", "password")

        verifyNoMoreInteractions(api)
    }
}

class LoginController(private val api: Login.Api,
                      private val view: Login.View,
                      private val emailChecker: Login.EmailChecker) {

    private val ERR_CANNOT_LOGIN = "Cannot login"
    private val ERR_WRONG_EMAIL = "Wrong email"

    fun onLogin(email: String, password: String) {
        if (password.isEmpty() || email.isEmpty()) {
            return
        }

        if (!emailChecker.verifyEmail(email)) {
            view.displayErrorMessage(ERR_WRONG_EMAIL)
            return
        }

        view.displayProgressBar()
        api.login(email, password)
                .doOnTerminate {
                    view.hideProgressBar()
                }
                .subscribe({
                    view.goToAnotherScreen()
                }, {
                    view.displayErrorMessage(ERR_CANNOT_LOGIN)
                })
    }
}

interface Login {
    interface Api {
        fun login(s: String, password: String): Observable<Unit>
    }

    interface View {
        fun displayErrorMessage(message: String)
        fun goToAnotherScreen()

        fun displayProgressBar()
        fun hideProgressBar()
    }

    interface EmailChecker {
        fun verifyEmail(email: String): Boolean
    }
}