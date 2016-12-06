package pl.krk.droidcon.workshops.login

interface UserSharedPreferences {
    fun saveUser(user: User)
}