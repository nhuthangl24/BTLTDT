package com.example.lab12.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab12.R
import com.example.lab12.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

/**
 * Demonstrates Pathway 1: Compose in Views
 * - Built on traditional Fragment & XML layout (fragment_home.xml).
 * - Implements ViewBinding safely tied to view lifecycle.
 * - Embeds a Composable (SummaryCard) using ComposeView.
 * - Navigates to DetailFragment using Navigation Component.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JuiceViewModel by activityViewModels()
    private lateinit var adapter: JuiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup traditional RecyclerView in View system
        adapter = JuiceAdapter { juice ->
            // In a full app, can navigate to view/edit details
        }
        binding.juiceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.juiceRecyclerView.adapter = adapter

        // 2. Setup Pathway 1: ComposeView inside XML layout
        binding.composeArea.apply {
            // Tie Composition lifecycle to Fragment's View tree lifecycle
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    val juices by viewModel.juiceList.collectAsState()
                    val total = juices.size
                    val avgRating = if (juices.isNotEmpty()) {
                        juices.map { it.rating }.average().toFloat()
                    } else {
                        0f
                    }

                    SummaryCard(
                        totalCount = total,
                        averageRating = avgRating,
                        onAddClicked = {
                            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
                        }
                    )
                }
            }
        }

        // 3. XML Button navigation
        binding.actionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }

        // 4. Observe ViewModel data for the View-based RecyclerView
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.juiceList.collect { juices ->
                    adapter.submitList(juices)
                    binding.emptyView.visibility = if (juices.isEmpty()) View.VISIBLE else View.GONE
                    binding.juiceRecyclerView.visibility = if (juices.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Prevent memory leaks by clearing ViewBinding reference
        _binding = null
    }
}

/**
 * SummaryCard: A Composable embedded in the traditional View hierarchy.
 * Follows Compose best practices:
 * - Purely declarative
 * - Receives data through parameters
 * - Emits actions through lambda callbacks
 */
@Composable
fun SummaryCard(
    totalCount: Int,
    averageRating: Float,
    onAddClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF673AB7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🍹",
                            fontSize = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Juice Stats (Compose in Views)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Embedded via ComposeView",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Juices",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$totalCount items",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = "Avg Rating",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = if (totalCount > 0) String.format("%.1f ★", averageRating) else "N/A",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFD54F)
                    )
                }

                Button(
                    onClick = onAddClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF673AB7)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "+ Add", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
