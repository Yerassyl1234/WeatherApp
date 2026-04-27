package com.example.weatherapp.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.theme.WeatherAppTheme
import com.example.weatherapp.core.ui.theme.spacing
import com.example.weatherapp.core.ui.theme.weatherColors

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
            )
            .background(MaterialTheme.weatherColors.card)
            .padding(horizontal = MaterialTheme.spacing.medium, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.weatherColors.textSecondary,
        )

        Spacer(Modifier.width(MaterialTheme.spacing.small))

        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.weatherColors.textSecondary,
                )
            }

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.weatherColors.textPrimary,
                ),
                cursorBrush = SolidColor(MaterialTheme.weatherColors.textPrimary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() },
                ),
            )
        }

        if (query.isNotBlank()) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.25f))
                    .clickable { onQueryChange("") },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.weatherColors.textPrimary,
                    modifier = Modifier.size(14.dp),
                )
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.microphone),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.weatherColors.textSecondary,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    WeatherAppTheme {
        SearchBar(
            query = "",
            onQueryChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}