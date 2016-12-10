package pl.krk.droidcon.workshops.login

class LoginController(private val api: Login.Api) {
    fun onLogin(email: String, password: String) {
        if ((password.length >= 3) and email.isNotEmpty() and password.isNotEmpty()) {
            api.login(email, password)
        }
    }
}