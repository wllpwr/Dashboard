package espresso

import android.util.TypedValue
import androidx.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.google.android.material.color.MaterialColors
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.String


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ThemeAndFontTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testThemeBlueTheme() {
        onView(withId(R.id.context_menu)).check(matches(isDisplayed()))

        onView(withId(R.id.context_menu)).perform(click())

        onView(withText("Settings")).check((matches(isDisplayed())))

        onView(withText("Settings")).perform(click())

        onView(withText("Theme")).check((matches(isDisplayed())))

        onView(withText("Theme")).perform(click())

        onView(withText("Blue")).check((matches(isDisplayed())))

        onView(withText("Blue")).perform(click())

        onView(withText("Confirm")).check((matches(isDisplayed())))

        onView(withText("Confirm")).perform(click())

        onView(withId(R.id.context_menu)).check(matches(isDisplayed()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val themePref = PreferenceManager.getDefaultSharedPreferences(context).all["theme"]

        assertEquals(themePref, "Blue")

        val blue = 2712319

        assertEquals(blue, MaterialColors.getColor(context, android.R.attr.colorPrimary, "No Color"))
    }

    @Test
    fun testFontRobotoMono() {
        onView(withId(R.id.context_menu)).check(matches(isDisplayed()))

        onView(withId(R.id.context_menu)).perform(click())

        onView(withText("Settings")).check((matches(isDisplayed())))

        onView(withText("Settings")).perform(click())

        onView(withText("Font")).check((matches(isDisplayed())))

        onView(withText("Font")).perform(click())

        onView(withText("Roboto Mono")).check((matches(isDisplayed())))

        onView(withText("Roboto Mono")).perform(click())

        onView(withText("Confirm")).check((matches(isDisplayed())))

        onView(withText("Confirm")).perform(click())

        onView(withId(R.id.context_menu)).check(matches(isDisplayed()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val font = PreferenceManager.getDefaultSharedPreferences(context).all["font_style"]

        assertEquals(font, "Roboto Mono")
    }
}