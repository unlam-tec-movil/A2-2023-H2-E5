package ar.edu.unlam.mobile.scaffold.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.unlam.mobile.scaffold.domain.kitty.models.Kitty
import coil.compose.AsyncImage

@Composable
fun PhotoItem(text: String, imageUrl: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column() {
            Row(
                Modifier.fillMaxWidth().height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.background(color = Color.Black, shape = CircleShape).size(40.dp),
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "avatar",
                    )
                }
                Column(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KittyCard(kitty: Kitty, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column() {
            Row(
                Modifier.fillMaxWidth().height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.background(color = Color.Black, shape = CircleShape).size(40.dp),
                ) {
                    AsyncImage(
                        model = kitty.url,
                        contentDescription = "avatar",
                    )
                }
                Column(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = kitty.id,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun KittyPreview() {
    KittyCard(
        kitty = Kitty(
            id = "Esto es un texto largo",
            url = "https://img.freepik.com/foto-gratis/cerrar-lindo-gato-interior_23-2148882585.jpg",
            width = 200,
            height = 300,
        ),
    )
}
