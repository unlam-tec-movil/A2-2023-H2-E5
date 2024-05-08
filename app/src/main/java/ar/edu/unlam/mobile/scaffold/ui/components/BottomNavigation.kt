import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffold.navigation.Route

@Composable
fun CustomBottomNavigation(
    items: List<BottomNavigationItemData>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onHomeSelected: () -> Unit,
    onSearchSelected: () -> Unit
) {
    BottomNavigation(
        modifier = Modifier.height(56.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            when (index) {
                                0 -> onHomeSelected()
                                1 -> onSearchSelected()
                            }
                        }
                    )
                },
                label = { Text(text = stringResource(item.title)) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}
 data class BottomNavigationItemData(
    val icon: ImageVector,
    val title: Int,
)