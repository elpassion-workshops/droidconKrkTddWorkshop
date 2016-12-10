package pl.krk.droidcon.workshops.login

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }

    interface View {
        fun showError(message: String)
        fun hideError()
    }
}