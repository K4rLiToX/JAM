package com.carlosdiestro.jobapplicationmanager.datasource

import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication

object FakeJobApplicationData {
    val jobApplications = listOf(
        JobApplication(
            "Android Developer",
            "Google",
            "Munich, Germany",
            applicationDate = 1629631183
        ),
        JobApplication(
            "QA Leader",
            "Accenture",
            "London, England",
            applicationDate = 1629631183
        ),
        JobApplication(
            "Junior Software Developer",
            "Amadeus",
            "Nize, France",
            applicationDate = 1629631183
        ),
        JobApplication(
            "Java Software Engineer",
            "OpenBank",
            "Madrid, Spain",
            applicationDate = 1629631183
        ),
        JobApplication(
            "Full Stack Engineer",
            "Facebook",
            "California, United States",
            applicationDate = 1629631183
        )
    )
}