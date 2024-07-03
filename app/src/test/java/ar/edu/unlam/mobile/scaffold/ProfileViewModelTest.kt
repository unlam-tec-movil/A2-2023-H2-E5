package ar.edu.unlam.mobile.scaffold

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.edu.unlam.mobile.scaffold.domain.model.ActivityLevel
import ar.edu.unlam.mobile.scaffold.domain.model.Gender
import ar.edu.unlam.mobile.scaffold.domain.model.GoalType
import ar.edu.unlam.mobile.scaffold.domain.model.UserInfo
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import ar.edu.unlam.mobile.scaffold.ui.screens.profile.ProfileViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ProfileViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var preferences: Preferences

    @Before
    fun setup() {
        preferences = mock(Preferences::class.java)
        viewModel = ProfileViewModel(preferences)
    }

    @Test
    fun testUpdateUserInfo() {
        // Arrange
        val userInfo = UserInfo(
            gender = Gender.Female,
            age = 30,
            height = 170,
            weight = 60.5f,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 40.0f,
            proteinRatio = 30.0f,
            fatRatio = 30.0f,
            steps = 8000
        )

        // Act
        viewModel.updateUserInfo(userInfo)

        // Assert
        verify(preferences).saveGender(userInfo.gender)
        verify(preferences).saveAge(userInfo.age)
        verify(preferences).saveWeight(userInfo.weight)
        verify(preferences).saveHeight(userInfo.height)
        verify(preferences).saveActivityLevel(userInfo.activityLevel)
        verify(preferences).saveGoalType(userInfo.goalType)
        verify(preferences).saveCarbRatio(userInfo.carbRatio)
        verify(preferences).saveProteinRatio(userInfo.proteinRatio)
        verify(preferences).saveFatRatio(userInfo.fatRatio)
        verify(preferences).saveStepsGoals(userInfo.steps)
    }
}