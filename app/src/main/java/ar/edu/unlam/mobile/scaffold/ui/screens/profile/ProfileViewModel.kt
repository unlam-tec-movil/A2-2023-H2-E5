
package ar.edu.unlam.mobile.scaffold.ui.screens.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffold.domain.model.UserInfo
import ar.edu.unlam.mobile.scaffold.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val preferences: Preferences,
    ) : ViewModel() {
        private val _profileImage = MutableStateFlow<Bitmap?>(null)
        val profileImage: StateFlow<Bitmap?> get() = _profileImage
        private val _userInfo = MutableLiveData<UserInfo>()
        val userInfo: LiveData<UserInfo> get() = _userInfo

        init {
            loadUserInfo()
        }

        private fun loadUserInfo() {
            _userInfo.value = preferences.loadUserInfo()
        }

        fun updateProfileImage(newImage: Bitmap) {
            _profileImage.value = newImage
        }

        fun updateUserInfo(updatedUserInfo: UserInfo) {
            _userInfo.value = updatedUserInfo
            preferences.saveGender(updatedUserInfo.gender)
            preferences.saveAge(updatedUserInfo.age)
            preferences.saveWeight(updatedUserInfo.weight)
            preferences.saveHeight(updatedUserInfo.height)
            preferences.saveActivityLevel(updatedUserInfo.activityLevel)
            preferences.saveGoalType(updatedUserInfo.goalType)
            preferences.saveCarbRatio(updatedUserInfo.carbRatio)
            preferences.saveProteinRatio(updatedUserInfo.proteinRatio)
            preferences.saveFatRatio(updatedUserInfo.fatRatio)
            preferences.saveStepsGoals(updatedUserInfo.steps)
        }
    }
