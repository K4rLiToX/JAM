package com.carlosdiestro.jobapplicationmanager.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.text.SimpleDateFormat
import java.util.*

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class NewJobApplicationFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickBackButton_popBackStack() {
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NewJobApplicationFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.btn_back_to_job_applications)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun clickAddJobApplicationButtonWithEmptyParameters_showWarningSnackBar() {
        launchFragmentInHiltContainer<NewJobApplicationFragment> {

        }

        onView(withId(R.id.btn_add_new_application)).perform(click())
        onView(withText(R.string.add_new_job_application_error)).check(matches(isDisplayed()))
    }

    @Test
    fun clickApplicationDateEditText_openCalendar() {
        launchFragmentInHiltContainer<NewJobApplicationFragment> {

        }

        onView(withId(R.id.et_application_date)).perform(click())
        onView(withText(R.string.date_picker_title)).check(matches(isDisplayed()))
    }

    @Test
    fun clickCalendarNegativeButton_dismissCalendar() {
        launchFragmentInHiltContainer<NewJobApplicationFragment> {

        }

        onView(withId(R.id.et_application_date)).perform(click())
        onView(withText("CANCEL")).perform(click())
        onView(withId(R.string.date_picker_title)).check(doesNotExist())
    }

    @Test
    fun clickCalendarPositiveButton_applicationDateEditTextWithTodayDate() {
        val todayDate = SimpleDateFormat("dd/MM/yyyy").format(Date()).toString()
        launchFragmentInHiltContainer<NewJobApplicationFragment> {

        }

        onView(withId(R.id.et_application_date)).perform(click())
        onView(withText("OK")).perform(click())
        onView(withId(R.id.et_application_date)).check(matches(withText(todayDate)))
    }

    @Test
    fun clickAddJobApplicationButtonWithParameters_navigateToJobApplicationFragment() {
        val jobPosition = "Android Developer"
        val company = "Google"
        val location = "Munich, Germany"
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<NewJobApplicationFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.et_job_position)).perform(
            typeText(jobPosition),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.et_company)).perform(typeText(company), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.et_location)).perform(
            typeText(location),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.et_application_date)).perform(click())
        onView(withText("OK")).perform(click())

        onView(withId(R.id.btn_add_new_application)).perform(click())

        verify(navController).popBackStack()
    }
}