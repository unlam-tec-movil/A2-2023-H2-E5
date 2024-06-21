package ar.edu.unlam.mobile.scaffold.data.repository.steps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StepsRepository {
    private var _stepGoal = MutableLiveData<Int>()
    val stepGoal: LiveData<Int> get() = _stepGoal

    fun setStepGoal(goal: Int) {
        _stepGoal.value = goal
    }
}
