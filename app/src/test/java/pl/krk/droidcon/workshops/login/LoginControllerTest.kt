package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.Subscription

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<View>()
    private val userRepository = mock<UserRepository>()
    private val controller = LoginController(api, view, userRepository)

    @Before
    fun setup() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(Unit))
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        login()
        verify(api).login("email@test.pl", "password")
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        login("email2@test.pl", "password")
        verify(api).login("email2@test.pl", "password")
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(email = "", password = "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveAtChar() {
        login("emailtest.pl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDot() {
        login("email@testpl", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailDoesntHaveDotAfterAtSign() {
        login("email.pl@test", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithPassword() {
        login()
        verify(api).login("email@test.pl", "password")
    }

    @Test
    fun shouldReallyCallApiWithPassword() {
        login(password = "pass")
        verify(api).login(any(), eq("pass"))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldOpenNextScreenOnApiCallSuccess() {
        whenever(api.login("email@test.pl", "passw")).thenReturn(Observable.just(Unit))
        login("email@test.pl", "passw")
        verify(view).openNextScreen()
    }

    @Test
    fun shouldDisplayErrorWhenApiCallError() {
        val wrongEmail = "wrong.email@test.pl"
        val wrongPass = "wrongPass"
        whenever(api.login(wrongEmail, wrongPass)).thenReturn(Observable.error(RuntimeException()))
        login(wrongEmail, wrongPass)
        verify(view).showErrorMessage()
    }

    @Test
    fun shouldNotOpenNextScreenWhenApiCallError() {
        val wrongEmail = "wrong.email@test.pl"
        val wrongPass = "wrongPass"
        whenever(api.login(wrongEmail, wrongPass)).thenReturn(Observable.error(RuntimeException()))
        login(wrongEmail, wrongPass)
        verify(view, never()).openNextScreen()
    }

    @Test
    fun shouldShowProgressWhenLoggingIn() {
        login()
        verify(view).showProgressLoader()
    }

    @Test
    fun shouldHideProgressWhenLoggedIn() {
        login()
        verify(view).hideProgressLoader()
    }

    @Test
    fun shouldNotHideProgressWhenNothingIsReturned() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        login()
        verify(view, never()).hideProgressLoader()
    }

    @Test
    fun shouldHideErrorMessageBeforeLogin() {
        login()
        verify(view).hideErrorMessage()
    }

    @Test
    fun shouldUnsubscribeWhenOnDestroyIsInvoked() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        login()
        controller.onDestroy()
        verify(view).hideProgressLoader()
    }

    @Test
    fun shouldNotHideProgressLoaderWhenCallIsFinished() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        login()
        controller.onDestroy()
        verify(view, times(1)).hideProgressLoader()
    }

    @Test
    fun shouldStoreUserData() {
        login()
        verify(userRepository).saveUser()
    }

    private fun login(email: String = "email@test.pl", password: String = "password") {
        controller.onLogin(email, password)
    }
}

class LoginController(private val api: Login.Api, private val view: View, private val userRepository: UserRepository) {

    var loginSubscripiton: Subscription? = null

    fun onLogin(email: String, password: String) {
        if (areCredentialsValid(email, password)) {
            performApiLogin(email, password)
        }
    }

    private fun areCredentialsValid(email: String, password: String) = isEmailValid(email) && password.isNotEmpty()

    private fun performApiLogin(email: String, password: String) {
        loginSubscripiton = api.login(email, password).doOnSubscribe {
            updateUiOnLoginClicked()
        }.doOnUnsubscribe {
            view.hideProgressLoader()
        }.subscribe({
            loginSuccess()
        }, {
            view.showErrorMessage()
        })
    }

    private fun loginSuccess() {
        userRepository.saveUser()
        view.openNextScreen()
    }

    private fun updateUiOnLoginClicked() {
        view.showProgressLoader()
        view.hideErrorMessage()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.matches(Regex(".*@.*\\..*"))
    }

    fun onDestroy() {
        loginSubscripiton?.unsubscribe()
    }
}
interface Login {

    interface Api {
        fun login(s: String, password: String): Observable<Unit>
    }
}
interface View {

    fun openNextScreen()
    fun showErrorMessage()
    fun showProgressLoader()
    fun hideProgressLoader()
    fun hideErrorMessage()
}
interface UserRepository {
    fun saveUser()
}
