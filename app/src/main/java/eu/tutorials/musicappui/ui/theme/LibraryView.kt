package eu.tutorials.musicappui.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.tutorials.musicappui.LibDummy
import eu.tutorials.musicappui.libraries

@Composable
fun Library() {
    LazyColumn {
        items(libraries) { it ->
            libraryItem(lib = it)
        }
    }
}

@Composable
fun libraryItem(lib: LibDummy) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding( 4.dp)) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = lib.drawable),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
            Text(text = lib.title, modifier = Modifier.padding(8.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End


        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    Divider(color = Color.LightGray, startIndent = 4.dp, thickness = 1.dp)
}

@Composable
@Preview(showBackground = true)
fun LibraryPreview() {
    Library()
}