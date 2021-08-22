package com.carlosdiestro.jobapplicationmanager.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.datasource.FakeJobApplicationData
import com.carlosdiestro.jobapplicationmanager.ui.adapters.JobApplicationAdapter
import com.carlosdiestro.jobapplicationmanager.utils.launchFragmentInHiltContainer
import com.google.gson.Gson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class JobApplicationsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickCleanNonPendingJobApplications_openDialogWarning() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_clean)).perform(click())
        onView(withText(R.string.clean_alert_dialog_title)).check(matches(isDisplayed()))
    }

    @Test
    fun clickWarningDialogNegativeButton_dismissDialogWarning() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_clean)).perform(click())
        onView(withText(R.string.clean_alert_dialog_negative_button_text)).perform(click())
        onView(withText(R.string.clean_alert_dialog_title)).check(doesNotExist())
    }

    @Test
    fun clickFilterJobApplications_openFilterMenu() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_filter)).perform(click())
        onView(withText(R.string.status_filter_menu_all)).check(matches(isDisplayed()))
    }

    @Test
    fun clickAllFilter_filterJobApplicationsByAll() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_filter)).perform(click())
        onView(withText(R.string.status_filter_menu_all)).perform(click())
        onView(withId(R.id.txt_show_filter)).check(matches(withText(R.string.status_filter_menu_all)))
    }

    @Test
    fun clickPendingFilter_filterJobApplicationsByAll() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_filter)).perform(click())
        onView(withText(R.string.status_filter_menu_pending)).perform(click())
        onView(withId(R.id.txt_show_filter)).check(matches(withText(R.string.status_filter_menu_pending)))
    }

    @Test
    fun clickAcceptedFilter_filterJobApplicationsByAll() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_filter)).perform(click())
        onView(withText(R.string.status_filter_menu_accepted)).perform(click())
        onView(withId(R.id.txt_show_filter)).check(matches(withText(R.string.status_filter_menu_accepted)))
    }

    @Test
    fun clickRejectedFilter_filterJobApplicationsByAll() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }

        onView(withId(R.id.btn_filter)).perform(click())
        onView(withText(R.string.status_filter_menu_rejected)).perform(click())
        onView(withId(R.id.txt_show_filter)).check(matches(withText(R.string.status_filter_menu_rejected)))
    }

    @Test
    fun clickAtAddJobApplicationItemButton_navigateToNewJobApplicationFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<JobApplicationsFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.btn_new_application)).perform(click())

        verify(navController).navigate(
            JobApplicationsFragmentDirections.jobApplicationsToNewJobApplication("")
        )
    }

    private val LIST_ITEM_IN_TEST = 4
    private val JOB_APPLICATION_IN_TEST = FakeJobApplicationData.jobApplications[4]

    @Test
    fun isRecyclerViewInvisible_onAppLaunchAndEmptyJobApplications() {
        launchFragmentInHiltContainer<JobApplicationsFragment> {

        }
        onView(withId(R.id.rv_job_applications)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun selectJobApplication_isNewJobApplicationFragmentVisible() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<JobApplicationsFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.rv_job_applications))
            .perform(
                actionOnItemAtPosition<JobApplicationAdapter.JobApplicationViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )
        verify(navController).navigate(
            JobApplicationsFragmentDirections.jobApplicationsToNewJobApplication(
                Gson().toJson(
                    JOB_APPLICATION_IN_TEST
                )
            )
        )
    }
}