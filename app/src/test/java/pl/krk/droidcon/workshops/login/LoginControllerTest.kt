package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable
import rx.schedulers.Schedulers
import rx.schedulers.TestScheduler

class LoginControllerTest {

    private val api = mock<Login.Api> {
        on { login(any(), any()) } doReturn Observable.never()
    }
    val view = mock<Login.View>()
    private val userStorage = mock<UserStorage>()

    private val controller = LoginController(api, view, userStorage, Schedulers.immediate(), Schedulers.immediate())

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
        controller.onLogin("email@test.pl", "password")
        verify(api).login(any(), eq("password"))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenApiCallFails() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException("login failed")))
        login()
        verify(view).showError()
    }

    @Test
    fun shouldNotShowErrorWhenApiCallSuccess() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(User("")))
        login()
        verify(view, never()).showError()
    }

    @Test
    fun shouldMoveToHomeScreenWhenApiCallSucceed() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(User("")))
        login()
        verify(view).gotoHomeScreen()
    }

    @Test
    fun shouldShowLoaderAfterClickingLoginButton() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun shouldNotShowLoaderWhenEmailIsEmpty() {
        login("", "some-password")
        verify(view, never()).showLoader()
    }

    @Test
    fun shouldHideLoaderAfterApiCallCompletes() {
        whenever(api.login(any(), any())) doReturn Observable.just(User(""))
        login("email@email.pl", "some-password")
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderUntilApiCallCompletes() {
        whenever(api.login(any(), any())) doReturn Observable.never()
        login("email@email.pl", "some-password")
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldHideLoaderWhenControllerCallsOnDestroy() {
        login("qwerty", "asdfgh")
        controller.onDestroy()
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderOnDestroyWhenNotShownBefore() {
        controller.onDestroy()
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldSaveUserDataWhenApiCallSucceeded() {
        whenever(api.login(any(), any())) doReturn Observable.just(User(""))
        login()
        verify(userStorage).saveUserData(User(""))
    }

    @Test
    fun shouldReallySaveUserDataWhenApiCallSucceeds() {
        whenever(api.login(any(), any())) doReturn Observable.just(User("user_1"))
        login()
        verify(userStorage).saveUserData(User("user_1"))
    }

    @Test
    fun shouldObserveOnPassedScheduler() {
        whenever(api.login(any(), any())) doReturn Observable.just(User("user_1"))
        val scheduler = TestScheduler()
        val controller = LoginController(api, view, userStorage, scheduler, Schedulers.immediate())
        controller.onLogin("dgf", "dshfgas")
        verify(userStorage, never()).saveUserData(User("user_1"))
        scheduler.triggerActions()
        verify(userStorage, times(1)).saveUserData(User("user_1"))
    }

    @Test§
    fun shouldSubscribeOnPassedScheduler() {
        whenever(api.login(any(), any())) doReturn Observable.just(User("user_1"))
        val scheduler = TestScheduler()
        val controller = LoginController(api, view, userStorage, Schedulers.immediate(), scheduler)
        controller.onLogin("dgf", "dshfgas")
        verify(view, never()).gotoHomeScreen()
        scheduler.triggerActions()
        verify(view, times(1)).gotoHomeScreen()
    }

    private fun login(email: String = "email@test.pl", password: String = "some-password") {
        controller.onLogin(email, password)
    }
}