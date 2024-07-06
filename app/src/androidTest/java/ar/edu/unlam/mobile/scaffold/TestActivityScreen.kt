package ar.edu.unlam.mobile.scaffold.ui.screens.activity

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ActivityScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun activityScreen_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityScreen(onNextClick = {})
        }
        // Verifica que el texto se muestra correctamente
        composeTestRule.onNodeWithText("Cual es tu nivel de actividad?").assertExists()

        // Verifica que las opciones de nivel de actividad se muestran correctamente
        composeTestRule.onNodeWithText("baja").assertExists()
        composeTestRule.onNodeWithText("Media").assertExists()
        composeTestRule.onNodeWithText("Alta").assertExists()

        // Verifica que el botón de siguiente se muestra correctamente
        composeTestRule.onNodeWithText("siguiente").assertExists()
    }

    @Test
    fun clickingNextButton_triggersOnNextClick() {
        var clicked = false
        composeTestRule.setContent {
            ActivityScreen(onNextClick = { clicked = true })
        }

        // Realiza un click en el botón de siguiente
        composeTestRule.onNodeWithText("Siguiente").performClick()

        // Verifica que se haya activado el callback onNextClick
        assert(clicked)
    }
}
