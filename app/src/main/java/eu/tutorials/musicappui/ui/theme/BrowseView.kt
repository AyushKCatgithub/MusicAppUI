package eu.tutorials.musicappui.ui.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import eu.tutorials.musicappui.R

@Composable
fun Browse() {
    val categories = listOf(
        "Hits",
        "Happy",
        "Workout",
        "Running",
        "Yoga",
        "Meditation",
        "TGIF",
        "Relax",
        "Focus",
        "Sleep",
        "Travel",
        "Cooking"
    )
    LazyVerticalGrid(GridCells.Fixed(2)) {
       items(categories){
           cat->
           BrowserItem(cat = cat, drawable = R.drawable.ic_baseline_25)
       }
    }
}
