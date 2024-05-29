package ar.edu.unlam.mobile.scaffold.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffold.R
import ar.edu.unlam.mobile.scaffold.core.util.UiEvent
import ar.edu.unlam.mobile.scaffold.domain.model.MealType
import ar.edu.unlam.mobile.scaffold.ui.components.DaySelector
import ar.edu.unlam.mobile.scaffold.ui.components.SearchTextField
import ar.edu.unlam.mobile.scaffold.ui.components.TrackableFoodItem
import ar.edu.unlam.mobile.scaffold.ui.screens.SearchEvent
import ar.edu.unlam.mobile.scaffold.ui.screens.SearchViewModel
import ar.edu.unlam.mobile.scaffold.ui.screens.TrackerOverviewEvent
import ar.edu.unlam.mobile.scaffold.ui.screens.TrackerOverviewViewmodel
import ar.edu.unlam.mobile.scaffold.ui.theme.LocalSpacing
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarNavigation(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit = { },
    viewmodel: TrackerOverviewViewmodel = hiltViewModel(),
    viewmodelSearch: SearchViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = viewmodel.state
    val stateSearch = viewmodelSearch.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        viewmodelSearch.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context),
                    )
                    keyboardController?.hide()
                }

                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium),
    ) {
        DaySelector(
            date = state.date,
            onPreviousDayClick = {
                viewmodel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
            },
            onNextDayClick = {
                viewmodel.onEvent(TrackerOverviewEvent.OnNextDayClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.spaceMedium),
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.h2,
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        SearchTextField(
            text = stateSearch.query,
            onValueChange = {
                viewmodelSearch.onEvent(SearchEvent.OnQueryChange(it))
            },
            onSearch = {
                keyboardController?.hide()
                viewmodelSearch.onEvent(SearchEvent.OnSearch)
            },
            onFocusChange = {
                viewmodelSearch.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
            },
            shouldShowHint = stateSearch.isHintVisible,
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(stateSearch.trackableFoods) { food ->
                TrackableFoodItem(
                    trackableFoodUiState = food,
                    onClick = {
                        viewmodelSearch.onEvent(
                            SearchEvent.OnToggleTrackableFood(food.food),
                        )
                    },
                    onAmountChange = {
                        viewmodelSearch.onEvent(
                            SearchEvent.OnAmountForFoodChange(
                                food.food,
                                it,
                            ),
                        )
                    },
                    onTrack = {
                        keyboardController?.hide()
                        viewmodelSearch.onEvent(
                            SearchEvent.OnTrackFoodClick(
                                food = food.food,
                                mealType = MealType.fromString(mealName),
                                date = LocalDate.of(year, month, dayOfMonth),
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            stateSearch.isSearching -> CircularProgressIndicator()
            stateSearch.trackableFoods.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}