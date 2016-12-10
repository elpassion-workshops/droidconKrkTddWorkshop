package pl.krk.droidcon.workshops.login

interface Login {
    interface Api {
        fun login(s: String, password: String)
    }
}