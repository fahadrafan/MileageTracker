package com.example.mileagetracker.ui.components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FGTopBar(
    title: String,
    showBack: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    showMenu: Boolean = false,
    onMenuClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {

    CenterAlignedTopAppBar(

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },

        navigationIcon = {

            when {

                showBack -> {

                    IconButton(onClick = { onBackClick?.invoke() }) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                showMenu -> {

                    IconButton(onClick = { onMenuClick?.invoke() }) {

                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            }
        },

        actions = {
            actions?.invoke()
        }
    )
}