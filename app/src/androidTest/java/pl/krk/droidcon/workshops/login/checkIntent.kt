package pl.krk.droidcon.workshops.login

import android.app.Activity
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import org.hamcrest.CoreMatchers

fun checkIntent(clazz: Class<out Activity>) {
    val allMatchers = IntentMatchers.hasComponent(clazz.name)
    Intents.intended(CoreMatchers.allOf(allMatchers))
}