package ar.edu.unlam.mobile.scaffold.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import ar.edu.unlam.mobile.scaffold.ui.components.KittyCard

@Composable
fun SecondaryScreen(controller: NavHostController, id: Int = 10) {
    Column {
        Text(text = "Hola soy la pantalla secundaria $id")
        KittyCard(
            kitty = Kitty(
                id = "$id",
                url = "https://img.freepik.com/foto-gratis/cerrar-lindo-gato-interior_23-2148882585.jpg",
                width = 200,
                height = 300,
            ),
        )
        Button(onClick = { controller.navigate("home") }) {
            Text(text = "Ir a la pantalla principal")
        }
    }
}
