package pl.krk.droidcon.workshops.login

import rx.Observable

object LoginApiProvider {
    val realApi: Login.Api by lazy {
        object : Login.Api {
            override fun login(login: String, password: String): Observable<User> {
                throw RuntimeException()
            }
        }
    }

    var override: Login.Api? = null

    fun get() : Login.Api {
        return override ?: realApi
    }
}
