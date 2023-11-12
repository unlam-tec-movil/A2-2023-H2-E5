package ar.edu.unlam.mobile.scaffold.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffold.R
import ar.edu.unlam.mobile.scaffold.ui.components.AddButton
import ar.edu.unlam.mobile.scaffold.ui.components.DaySelector
import ar.edu.unlam.mobile.scaffold.ui.components.ExpandableMeal
import ar.edu.unlam.mobile.scaffold.ui.components.NutrientsHeader
import ar.edu.unlam.mobile.scaffold.ui.components.TrackedFoodItem
import ar.edu.unlam.mobile.scaffold.ui.theme.LocalSpacing

@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit,
    viewmodel: TrackerOverviewViewmodel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = viewmodel.state
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(bottom = spacing.spaceMedium),
    ) {
        item {
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            DaySelector(
                date = state.date,
                onPreviousDayClick = {
                    viewmodel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    viewmodel.onEvent(TrackerOverviewEvent.OnNextDayClick)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.spaceMedium),
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        items(state.meals) { meal ->
            ExpandableMeal(
                meal = meal,
                onToggleClick = {
                    viewmodel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                },
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.spaceSmall),
                    ) {
                        val foods = state.trackedFoods.filter {
                            it.mealType == meal.mealType
                        }
                        foods.forEach { food ->
                            TrackedFoodItem(
                                trackedFood = food,
                                onDeleteClick = {
                                    viewmodel.onEvent(
                                        TrackerOverviewEvent.OnDeleteTrackedFoodClick(food),
                                    )
                                },
                            )
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        }
                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context),
                            ),
                            onClick = {
                                onNavigateToSearch(
                                    meal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
