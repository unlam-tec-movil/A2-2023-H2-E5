package ar.edu.unlam.mobile.scaffold.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ar.edu.unlam.mobile.scaffold.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun parseDateText(
    date: LocalDate,
): String {
    val today = LocalDate.now()
    return when (date) {
        today -> stringResource(id = R.string.today)
        today.minusDays(1) -> stringResource(id = R.string.yesterday)
        today.plusDays(1) -> stringResource(id = R.string.tomorrow)
        else -> DateTimeFormatter.ofPattern("dd LLLL").format(date)
    }
}
