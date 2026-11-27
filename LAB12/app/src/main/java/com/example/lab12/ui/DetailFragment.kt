package com.example.lab12.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab12.data.JuiceColor
import com.example.lab12.databinding.LayoutLegacyJuicePreviewBinding

/**
 * Demonstrates Pathway 2: Views in Compose
 * - Screen hosted via ComposeView.
 * - Compose UI handles inputs (Name, Description, Color).
 * - AndroidView integrates a legacy View widget (RatingBar) with factory & update.
 * - AndroidViewBinding inflates an existing XML layout (LayoutLegacyJuicePreviewBinding).
 */
class DetailFragment : Fragment() {

    private val viewModel: JuiceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AddJuiceScreen(
                            onSave = { name, description, color, rating ->
                                viewModel.addJuice(name, description, color, rating)
                                Toast.makeText(requireContext(), "Juice added successfully!", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            },
                            onCancel = {
                                findNavController().popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddJuiceScreen(
    onSave: (name: String, description: String, color: JuiceColor, rating: Int) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(JuiceColor.Orange) }
    var rating by remember { mutableIntStateOf(5) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Add New Juice",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Demonstrating Pathway 2: AndroidView & AndroidViewBinding",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Juice Name") },
            placeholder = { Text("e.g. Mango Passionfruit") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description & Ingredients") },
            placeholder = { Text("e.g. 1 mango, 2 passion fruits, crushed ice") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Color Selection (Compose UI)
        Text(
            text = "Juice Color:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            JuiceColor.values().forEach { juiceColor ->
                val isSelected = juiceColor == selectedColor
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(juiceColor.composeColorHex))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = juiceColor },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Text(text = "✓", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // =========================================================================
        // Pathway 2 - Integration A: AndroidView hosting traditional RatingBar
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Rating (AndroidView -> RatingBar): $rating / 5",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF33691E)
                )
                Text(
                    text = "Traditional View widget embedded and synchronized inside Compose tree.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF558B2F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                AndroidView(
                    modifier = Modifier.padding(vertical = 4.dp),
                    factory = { context ->
                        // 1. factory: creates View initially
                        RatingBar(context).apply {
                            numStars = 5
                            stepSize = 1.0f
                            setIsIndicator(false)
                            setOnRatingBarChangeListener { _, newRating, fromUser ->
                                if (fromUser) {
                                    rating = newRating.toInt()
                                }
                            }
                        }
                    },
                    update = { ratingBar ->
                        // 2. update: synchronizes View when Compose state changes without recreating View
                        if (ratingBar.rating != rating.toFloat()) {
                            ratingBar.rating = rating.toFloat()
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =========================================================================
        // Pathway 2 - Integration B: AndroidViewBinding reusing XML layout
        // =========================================================================
        AndroidViewBinding(
            factory = { inflater, parent, attach ->
                LayoutLegacyJuicePreviewBinding.inflate(inflater, parent, attach)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Accessing XML binding safely in Compose
            titleText.text = if (name.isNotBlank()) "Preview: $name" else "Legacy XML Preview"
            previewDescription.text = if (description.isNotBlank()) description else "Type in fields above to see live updates in XML binding!"
            actionButton.setOnClickListener {
                Toast.makeText(root.context, "XML Action clicked for: ${if (name.isNotBlank()) name else "Juice"}", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }

            Button(
                onClick = {
                    if (name.isBlank()) {
                        name = "Fresh " + selectedColor.name + " Juice"
                    }
                    onSave(name, description, selectedColor, rating)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save Juice")
            }
        }
    }
}
