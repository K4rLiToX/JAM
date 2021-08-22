package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.databinding.FragmentGetStartedBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class GetStartedFragment : Fragment() {

    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpStatusBarColor()
        binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpStatusBarColor() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.light_grey)
        WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightStatusBars = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewStyle()
        handleMotionLayoutTransitionListener()
    }

    private fun setUpViewStyle() {
        val shapeAppearanceModel =
            ShapeAppearanceModel().toBuilder().setTopRightCorner(CornerFamily.CUT, 300F).build()
        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        shapeDrawable.fillColor = ContextCompat.getColorStateList(requireContext(), R.color.black)
        ViewCompat.setBackground(binding.getStartedLayout, shapeDrawable)
    }

    private fun handleMotionLayoutTransitionListener() =
        binding.getStartedMotionLayout.setTransitionListener(
            object : TransitionAdapter() {

                override fun onTransitionCompleted(
                    motionLayout: MotionLayout?,
                    currentId: Int
                ) {
                    findNavController().navigate(R.id.getStartedToJobApplications)
                }
            }
        )
}
