package pl.krk.droidcon.workshops.login

import rx.Observable

object LoginApiProvider {
    val realApi: Login.Api by lazy {
        object : Login.Api {
            override fun login(login: String, password: String): Observable<User> {
                return Observable.error(RuntimeException("some failure"))
            }
        }
    }

    var override: Login.Api? = null

    fun get(): Login.Api {
        return override ?: realApi
    }
}