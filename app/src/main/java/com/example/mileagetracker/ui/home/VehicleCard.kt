package com.example.mileagetracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VehicleCard(
    name: String,
    type: String,
    mileage: Double?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = if (type == "CAR") "🚗" else "🏍"
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(type)

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = mileage?.let {
                        if (it > 0) "%.1f km/l".format(it)
                        else "-- km/l"
                    } ?: "-- km/l",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}