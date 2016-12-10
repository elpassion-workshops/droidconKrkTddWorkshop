package pl.krk.droidcon.workshops.login

import rx.Observable

object LoginApiProvider {
    val realApi: Login.Api by lazy {
        object : Login.Api {
            override fun login(login: String, password: String): Observable<User> {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    var override: Login.Api? = null

    fun get(): Login.Api {
        return override ?: realApi
    }
}