package pl.krk.droidcon.workshops

abstract class Provider<T>(initializer: () -> T) {

    private val original by lazy { initializer() }

    fun get() = override?.invoke() ?: original

    var override: (() -> T)? = null
}