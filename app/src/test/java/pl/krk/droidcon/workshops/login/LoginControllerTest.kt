package pl.krk.droidcon.workshops.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable
import rx.schedulers.TestScheduler

class LoginControllerTest {

    private val observeOnScheduler = TestScheduler()
    private val subscribeOnScheduler = TestScheduler()
    private val api = mock<Login.Api>()
    private val storage = mock<Login.Storage>()
    private val view = mock<Login.View>()
    private val controller = LoginController(api, storage, view, observeOnScheduler,
            subscribeOnScheduler)
    private val user = User(token = "droicon krakow rulez!")

    @Test
    fun shouldCallApiWithProvidedEmail() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(api).login("email@test.pl", "mypass")
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldReallyCallApiWithProvidedEmail() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(api).login("email@test.pl", "mypass")
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    private fun login(login: String = "email@test.pl", password: String = "mypass") {
        controller.onLogin(login, password)
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(login = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        verify(api).login("email@test.pl", "mypass")
    }

    @Test
    fun shouldNotCallLoginWithEmptyPassword() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallLoginIfLoginIsCorrectAndPasswordIsEmpty() {
        login(login = "123", password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldNotCallLoginIfPasswordIsNotHaveValidFormat() {
        login("name@test.pl", password = "12")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowErrorWhenLoginFails() {
        login(login = "")
        verify(view).showError(any())
    }

    @Test
    fun shouldNotShowErrorMessageOnSuccessfulLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(view, never()).showError(any())
        verify(view).showLoadProgress()
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldShowErrorMessageOnFailedLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(view).showLoadProgress()
        verify(view).showError(any())
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldHideLoaderOnDestroy() {
        whenever(api.login(any(), any())).thenReturn(Observable.never())
        login()
        controller.onDestroy()
        verify(view).hideLoadProgress()
    }

    @Test
    fun shouldOpenMainScreenAfterSuccessfulLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(view).showMainScreen()
    }

    @Test
    fun shouldStoreUserInPersistentStoreAfterSuccessfulLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        login()
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(storage).setUser(user)
    }

    @Test
    fun shouldNotStoreUserInPersistentStoreAfterFailedLogin() {
        whenever(api.login(any(), any())).thenReturn(Observable.error(RuntimeException()))
        login()
        verify(storage, never()).setUser(user)
    }

    @Test
    fun shouldObserveOnProvidedScheduler() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        LoginController(api, storage, view, observeOnScheduler, subscribeOnScheduler).onLogin("email@test.pl","password")
        verify(view, never()).showMainScreen()
    }

    @Test
    fun shouldSubscribeOnProvidedScheduler() {
        whenever(api.login(any(), any())).thenReturn(Observable.just(user))
        LoginController(api, storage, view, observeOnScheduler, subscribeOnScheduler).onLogin("email@test.pl", "password")
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
        verify(view).showMainScreen()
    }
}
