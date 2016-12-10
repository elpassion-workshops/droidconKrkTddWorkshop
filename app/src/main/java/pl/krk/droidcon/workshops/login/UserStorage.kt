package pl.krk.droidcon.workshops.login

interface UserStorage {
    fun saveUserData(user: User)
}