package ar.edu.unlam.mobile.scaffold.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import ar.edu.unlam.mobile.scaffold.ui.components.PhotoCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    // La información que obtenemos desde el view model la consumimos a través de un estado de "tres vías".
    // Loading, Success y Error. Esto nos permite mostrar un estado de carga, un estado de éxito y un mensaje de error.
    val uiState: HomeUIState by viewModel.uiState.collectAsState()

    when (val kittyState = uiState.kittyState) {
        is KittyUIState.Loading -> {
            CircularProgressIndicator()
        }

        is KittyUIState.Success -> {
            Body(kitty = kittyState.kitty, action = viewModel::getKitty, modifier = modifier)
        }

        is KittyUIState.Error -> {
            // Error
        }
    }
}

@Composable
fun Body(kitty: Kitty, action: () -> Unit, modifier: Modifier = Modifier) {
    Column {
        PhotoCard(text = kitty.id, title = kitty.url, imageUrl = kitty.url, action = action, modifier = modifier)
    }
}
