package cz.uhk.fim.footballapp.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.uhk.fim.footballapp.R

@Composable
fun DeleteFavouriteTeamConfirmDialog(
    teamName: String,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = stringResource(R.string.delete_confirmation_title))
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.delete_confirmation_message, teamName))

                Spacer(modifier = Modifier.height(8.dp))

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmDelete()
                },
            ) {
                Text(stringResource(R.string.dialog_remove))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.dialog_cancel))
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}
