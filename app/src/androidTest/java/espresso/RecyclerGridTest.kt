package espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.model.Atoms.getCurrentUrl
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.capstone.AddWidgetRecycler
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.RecyclerGrid
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RecyclerGridTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAddWidget() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollTo<AddWidgetRecycler.ViewHolder>(
            hasDescendant(withText("Weather Widget"))))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItem<AddWidgetRecycler.ViewHolder>(
            hasDescendant(withText("Weather Widget")), click()))

        onView(withId(R.id.fab)).check(matches(isDisplayed()))

        onWebView().forceJavascriptEnabled()

        onWebView().check(webMatches(getCurrentUrl(), containsString("https://appassets.androidplatform.net/assets/WeatherWidget.html")))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerGrid.ViewHolder>(0, swipeLeft()))
    }

    @Test
    fun testTimeWidgetSettings() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollTo<AddWidgetRecycler.ViewHolder>(
            hasDescendant(withText("Time Widget"))))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItem<AddWidgetRecycler.ViewHolder>(
            hasDescendant(withText("Time Widget")), click()))

        onView(withId(R.id.fab)).check(matches(isDisplayed()))

        onWebView().forceJavascriptEnabled()

        onWebView().check(webMatches(getCurrentUrl(), containsString("https://appassets.androidplatform.net/assets/TimeWidget.html")))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerGrid.ViewHolder>(0, swipeRight()))

        onView(withText("Time Zone")).check(matches(isDisplayed()))

        onView(withText("Time Zone")).perform(click())

        onView(withText("UTC")).check(matches(isDisplayed()))

        onView(withText("UTC")).perform(click())

        onView(withText("Confirm")).check(matches(isDisplayed()))

        onView(withText("Confirm")).perform(click())

        onWebView().withElement(findElement(Locator.ID, "timeZone")).check(webMatches(getText(), containsString("UTC")))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerGrid.ViewHolder>(0, swipeLeft()))
    }

}