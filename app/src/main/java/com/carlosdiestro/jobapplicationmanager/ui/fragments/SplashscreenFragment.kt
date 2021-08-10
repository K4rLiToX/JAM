package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.jobapplicationmanager.R

class SplashscreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splashscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToJobApplications()
    }

    private fun navigateToJobApplications() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.splashscreenToJobApplications)
        }, 1000)
    }
}