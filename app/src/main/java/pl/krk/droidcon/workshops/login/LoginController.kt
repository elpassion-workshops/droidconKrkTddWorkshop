package pl.krk.droidcon.workshops.login

class LoginController(private val api: Login.Api, private val view: Login.View) {

    fun onLogin(email: String, password: String) {
        if ((password.length >= 3) and email.isNotEmpty() and password.isNotEmpty()) {
            view.hideError()
            api.login(email, password).subscribe({ unit ->  } ,
                    { throwable -> view.showError("API login failed")})
        } else {
            view.showError("Invalid login or password!")
        }
    }
}