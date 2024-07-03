import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.rule.GrantPermissionRule
import ar.edu.unlam.mobile.scaffold.domain.model.Point
import ar.edu.unlam.mobile.scaffold.ui.screens.map.MapScreen
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import org.junit.Rule
import org.junit.Test

class MapTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )

    @Test
    fun whenHavingDefaultStatesInMapUi_VerifyIfAllViewsExists() {
        composeTestRule.setContent {
            MapScreen(
                    currentPosition = LatLng(0.0, 0.0),
                    cameraState = CameraPositionState(),
                    puntosDeEncuentro = listOf(Point(10.0, 10.0), Point(20.0, 20.0)),
                )
            }
        }
        composeTestRule.onNodeWithTag(testTag = "MapScreen title").assertExists()
        composeTestRule.onNodeWithTag(testTag = "MapScreen googleMap").assertExists()
    }
}
