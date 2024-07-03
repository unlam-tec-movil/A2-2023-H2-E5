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
        // Estado mutable para la imagen de perfil
        private val _profileImage = MutableStateFlow<Bitmap?>(null)
        val profileImage: StateFlow<Bitmap?> get() = _profileImage

        // LiveData para la información del usuario
        private val _userInfo = MutableLiveData<UserInfo>()
        val userInfo: LiveData<UserInfo> get() = _userInfo

        // Carga inicial de la información del usuario desde las preferencias
        init {
            loadUserInfo()
        }

        // Método privado para cargar la información del usuario
        private fun loadUserInfo() {
            _userInfo.value = preferences.loadUserInfo()
        }

        // Actualiza la imagen de perfil con una nueva imagen
        fun updateProfileImage(newImage: Bitmap) {
            _profileImage.value = newImage
        }

        // Actualiza la información del usuario y guarda en las preferencias
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
