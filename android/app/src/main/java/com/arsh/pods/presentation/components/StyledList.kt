package com.arsh.pods.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsh.pods.presentation.theme.PodsTheme

@Composable
fun StyledList(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    content: @Composable StyledListScope.() -> Unit
) {
    val scope = StyledListScope()
    scope.content()
    val focusBlockShape = RoundedCornerShape(28.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        title?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(focusBlockShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            scope.items.forEachIndexed { index, item -> item(index, scope.items.size) }
        }

        description?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
    }
}

class StyledListScope {
    internal val items = mutableListOf<@Composable (Int, Int) -> Unit>()

    fun item(content: @Composable (index: Int, count: Int) -> Unit) {
        items += content
    }
}

@Preview(showBackground = true)
@Composable
fun StyledListDemo() {
    PodsTheme {
        Column(Modifier.padding(16.dp)) {
            StyledList(title = "Audio") {
                for (i in 0..2) StyledListItem(name = "Setting ${i + 1}", onClick = {})
                val checked = remember { mutableStateOf(false) }
                StyledToggle(
                    label = "Personalized volume",
                    description = "Adjusts media volume to suit your surroundings.",
                    checked = checked.value,
                    onCheckedChange = { checked.value = it }
                )
            }
        }
    }
}
