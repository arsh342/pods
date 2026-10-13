package com.arsh.pods.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StyledListItem(
    modifier: Modifier = Modifier,
    title: String? = null,
    name: String,
    onClick: (() -> Unit)?,
    description: String? = null,
    height: Dp = 64.dp,
    enabled: Boolean = true,
    orientation: ListItemOrientation = ListItemOrientation.Horizontal,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        title?.let { SectionTitle(it) }
        Column {
            StyledListItemContent(
                name = name,
                onClick = onClick,
                description = description,
                height = height,
                enabled = enabled,
                index = 0,
                count = 1,
                orientation = orientation,
                leadingContent = leadingContent,
                trailingContent = trailingContent
            )
        }
    }
}

@Composable
fun StyledListScope.StyledListItem(
    modifier: Modifier = Modifier,
    name: String,
    onClick: (() -> Unit)? = null,
    description: String? = null,
    enabled: Boolean = onClick != null,
    orientation: ListItemOrientation = ListItemOrientation.Horizontal,
    selected: Boolean? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    item { index, count ->
        StyledListItemContent(
            name = name,
            onClick = onClick,
            description = description,
            enabled = enabled,
            index = index,
            count = count,
            orientation = orientation,
            modifier = modifier,
            selected = selected,
            leadingContent = leadingContent,
            trailingContent = trailingContent
        )
    }
}

enum class ListItemOrientation { Horizontal, Vertical }

@Composable
private fun StyledListItemContent(
    modifier: Modifier = Modifier,
    name: String,
    onClick: (() -> Unit)?,
    description: String? = null,
    height: Dp = 64.dp,
    enabled: Boolean = true,
    index: Int,
    count: Int,
    orientation: ListItemOrientation = ListItemOrientation.Horizontal,
    selected: Boolean? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val interactive = enabled && onClick != null
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = height)
                .then(
                    if (interactive) Modifier.clickable(role = Role.Button, onClick = onClick!!) else Modifier
                )
                .padding(horizontal = 16.dp, vertical = if (orientation == ListItemOrientation.Vertical) 12.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent?.let {
                it()
                Spacer(Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    maxLines = if (orientation == ListItemOrientation.Horizontal && description == null) 2 else 1,
                    overflow = TextOverflow.Ellipsis
                )
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (orientation == ListItemOrientation.Vertical) 4 else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            when {
                trailingContent != null -> {
                    Spacer(Modifier.width(12.dp))
                    trailingContent()
                }
                selected == true -> {
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(dev.oneuiproject.oneui.R.drawable.ic_oui_selected),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                interactive -> {
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(dev.oneuiproject.oneui.R.drawable.ic_oui_arrow_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (index < count - 1) {
            HorizontalDivider(
                modifier = Modifier.padding(start = if (leadingContent != null) 68.dp else 20.dp, end = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 8.dp)
    )
}
