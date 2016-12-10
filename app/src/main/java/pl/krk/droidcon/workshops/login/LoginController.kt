package pl.krk.droidcon.workshops.login

class LoginController(private val api: Login.Api, private val view : Login.View) {

    private val EMAIL_PATTERN = ".+@.+".toRegex()

    fun onLogin(email: String, password: String) {
        if (password.isEmpty() || !email.isEmailValid()) {
            return
        }
        view.showLoader()
        api.login(email, password).subscribe(
                {view.openNextScreen()},
                {view.showError()})
    }

    private fun String.isEmailValid(): Boolean {
        return isNotEmpty() && matches(EMAIL_PATTERN)
    }

}