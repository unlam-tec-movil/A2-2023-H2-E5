package ar.edu.unlam.mobile.scaffold.ui.screens.goal

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffold.domain.model.GoalType
import ar.edu.unlam.mobile.scaffold.core.util.UiEvent
import ar.edu.unlam.mobile.scaffold.ui.LocalSpacing
import ar.edu.unlam.mobile.scaffold.ui.components.ActionButton
import ar.edu.unlam.mobile.scaffold.ui.components.SelectableButton
import ar.edu.unlam.mobile.scaffold.R

@Composable
fun GoalScreen(
    onNextClick: () -> Unit,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNextClick()
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.lose_keep_or_gain_weight),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row {
                SelectableButton(
                    text = stringResource(id = R.string.lose),
                    isSelected = viewModel.selectedGoalType is GoalType.LoseWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = { viewModel.onGoalTypeClick(GoalType.LoseWeight) },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                SelectableButton(
                    text = stringResource(id = R.string.keep),
                    isSelected = viewModel.selectedGoalType is GoalType.KeepWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = { viewModel.onGoalTypeClick(GoalType.KeepWeight) },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                SelectableButton(
                    text = stringResource(id = R.string.gain),
                    isSelected = viewModel.selectedGoalType is GoalType.GainWeight,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedTextColor = Color.White,
                    onClick = { viewModel.onGoalTypeClick(GoalType.GainWeight) },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )

    }
}