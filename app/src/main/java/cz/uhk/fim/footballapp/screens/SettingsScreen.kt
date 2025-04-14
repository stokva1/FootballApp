package cz.uhk.fim.footballapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cz.uhk.fim.footballapp.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
//    cryptoRatesViewModel: CryptoRatesViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
//    val rates by cryptoRatesViewModel.rates.collectAsState()
    val preferredRateId by settingsViewModel.preferredRateId.collectAsState()


    var selectedRateId by remember { mutableStateOf( "") }
    var selectedRateName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) { cryptoRatesViewModel.getRates() }

    LaunchedEffect(preferredRateId) {
        selectedRateId = preferredRateId ?: ""
    }
}