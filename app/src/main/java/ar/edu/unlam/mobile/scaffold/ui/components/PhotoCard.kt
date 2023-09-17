package ar.edu.unlam.mobile.scaffold.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// https://www.develou.com/cards-en-jetpack-compose/
@Composable
fun PhotoCard(text: String, title: String, imageUrl: String, action: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column() {
            Row(
                Modifier.fillMaxWidth().height(250.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Multimedia de tarjeta",
                    Modifier
                        .background(color = Color.LightGray)
                        .fillMaxWidth()
                        .height(194.dp),
                )
            }
            Row(
                Modifier.fillMaxWidth().height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth().height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
            ) {
                // Botones

                // Iconos
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(onClick = action) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                    }
                }
            }
        }
    }
}
