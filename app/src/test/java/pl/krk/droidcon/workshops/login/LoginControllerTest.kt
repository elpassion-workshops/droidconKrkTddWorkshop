package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import rx.Observable

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val facebook = mock<Login.Facebook>()
    private val sharedPreferences: UserSharedPreferences = mock()
    private val controller = LoginController(api, view, sharedPreferences, facebook)

    @Before
    fun setUp() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
    }

    @Test
    fun shouldShowErrorWhenLoginIsEmpty() {
        loginWithCredentials(login = "")
        verify(view, times(1)).showEmptyCredentialError()
    }

    @Test
    fun shouldNotCallApiIfLoginIsEmpty() {
        loginWithCredentials(login = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenPasswordIsEmpty() {
        loginWithCredentials(password = "")
        verify(view, times(1)).showEmptyCredentialError()
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        loginWithCredentials(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        stubApiToReturnOnCredentials(login = "login@test.pl", password = "password", returnValue = Observable.error(RuntimeException()))
        loginWithCredentials(login = "login@test.pl", password = "password")
        verify(view, times(1)).showLoginFailedError()
    }

    @Test
    fun shouldOpenNextScreenIfApiCallSucceed() {
        stubApiToReturnOnCredentials(login = "login1@test.pl", password = "password123", returnValue = Observable.just(createUser()))
        loginWithCredentials(login = "login1@test.pl", password = "password123")
        verify(view, times(1)).openNextScreen()
    }

    @Test
    fun shouldShowLoaderOnApiCallStart() {
        loginWithCredentials()
        verify(view, times(1)).showLoader()
    }

    @Test
    fun shouldHideLoaderOnApiCallEnd() {
        stubApiToReturnOnCredentials(returnValue = Observable.empty())
        loginWithCredentials()
        verify(view, times(1)).hideLoader()
    }

    @Test
    fun shouldHideLoaderOnDestroyIfCallIsStillInProgress() {
        stubApiToReturnOnCredentials(returnValue = Observable.never())
        loginWithCredentials()
        controller.onDestroy()
        verify(view, times(1)).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderOnDestroyIfCallHasNotBeenStarted() {
        controller.onDestroy()
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldShowInvalidEmailErrorWhenEmailIsNotValid() {
        loginWithCredentials("invalidEmail.pl", "password")
        verify(view).showInvalidEmailError()
    }

    @Test
    fun shouldNotCallApiWhenEmailIsInvalid() {
        loginWithCredentials("invalidEmail", "password")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowInvalidEmailErrorWhenEmailContainsAtSignAndStillIsInvalid() {
        loginWithCredentials("invalidEmail@@", "password")
        verify(view).showInvalidEmailError()
    }

    @Test
    fun shouldSaveUserWithId_1_ToSharedPreferencesIfUserWithThisId() {
        stubApiToReturnOnCredentials(returnValue = Observable.just(createUser(id = 1)))
        loginWithCredentials()
        verify(sharedPreferences, times(1)).saveUser(createUser(id = 1))
    }

    @Test
    fun shouldReallySaveToSharedPreferencesUserFromApi() {
        stubApiToReturnOnCredentials(returnValue = Observable.just(createUser(id = 2)))
        loginWithCredentials()
        verify(sharedPreferences, times(1)).saveUser(createUser(id = 2))
    }

    @Test
    fun shouldSetupViewWithFacebookButtonOnCreate() {
        val facebookButtonProvider = mock<Login.FacebookButtonProvider>()
        whenever(facebook.getFacebookButtonProvider()).thenReturn(facebookButtonProvider)
        controller.onCreate()
        verify(view).setupFacebookButton(facebookButtonProvider)
    }

    private fun createUser(id: Int = 1) = User(id)

    private fun stubApiToReturnOnCredentials(login: String = any(), password: String = any(), returnValue: Observable<User>) {
        whenever(api.login(login, password)).thenReturn(returnValue)
    }

    private fun loginWithCredentials(login: String = "login@test.pl", password: String = "password") {
        controller.onLogin(login, password)
    }

}