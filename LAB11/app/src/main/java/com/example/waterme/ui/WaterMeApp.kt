/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.waterme.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.waterme.FIVE_SECONDS
import com.example.waterme.ONE_DAY
import com.example.waterme.R
import com.example.waterme.SEVEN_DAYS
import com.example.waterme.THIRTY_DAYS
import com.example.waterme.data.DataSource
import com.example.waterme.data.Reminder
import com.example.waterme.model.Plant
import com.example.waterme.model.PlantText
import com.example.waterme.ui.theme.WaterMeTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun PlantText.asString(): String = when (this) {
    is PlantText.Resource -> stringResource(resId)
    is PlantText.Plain -> value
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterMeApp(waterViewModel: WaterViewModel = viewModel(factory = WaterViewModel.Factory)) {
    val plants by waterViewModel.plants.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showAddPlantDialog by rememberSaveable { mutableStateOf(false) }

    WaterMeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddPlantDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = "+",
                        style = typography.headlineSmall
                    )
                }
            }
        ) { innerPadding ->
            PlantListContent(
                plants = plants,
                onScheduleReminder = { reminder ->
                    waterViewModel.scheduleReminder(reminder)
                    val label = reminder.durationRes?.let { context.getString(it) }
                        ?: reminder.customLabel
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.reminder_scheduled,
                                reminder.plantName,
                                label
                            )
                        )
                    }
                },
                onDeletePlant = { plant, plantName ->
                    waterViewModel.deletePlant(plant)
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.plant_deleted, plantName)
                        )
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
        }

        if (showAddPlantDialog) {
            AddPlantDialog(
                onDismiss = { showAddPlantDialog = false },
                onSavePlant = { name, type, description, schedule ->
                    waterViewModel.addPlant(name, type, description, schedule)
                    showAddPlantDialog = false
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.plant_added, name)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PlantListContent(
    plants: List<Plant>,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier,
    onDeletePlant: (Plant, String) -> Unit = { _, _ -> }
) {
    var selectedPlantId by rememberSaveable { mutableStateOf<String?>(null) }
    var deletePlantId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedPlant = plants.find { it.id == selectedPlantId }
    val plantToDelete = plants.find { it.id == deletePlantId }

    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)
    if (plants.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(mediumPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.empty_plants_message),
                style = typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(
                start = mediumPadding,
                top = mediumPadding,
                end = mediumPadding,
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            modifier = modifier
        ) {
            items(items = plants, key = { it.id }) {
                PlantListItem(
                    plant = it,
                    onItemSelect = { plant -> selectedPlantId = plant.id },
                    onDeleteClick = { plant -> deletePlantId = plant.id },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (selectedPlant != null) {
        ReminderDialogContent(
            onDialogDismiss = { selectedPlantId = null },
            plantName = selectedPlant.name.asString(),
            onScheduleReminder = onScheduleReminder
        )
    }

    if (plantToDelete != null) {
        val plantName = plantToDelete.name.asString()
        AlertDialog(
            onDismissRequest = { deletePlantId = null },
            title = { Text(stringResource(R.string.delete_plant_title)) },
            text = { Text(stringResource(R.string.delete_plant_confirm, plantName)) },
            confirmButton = {
                Button(
                    onClick = {
                        deletePlantId = null
                        onDeletePlant(plantToDelete, plantName)
                    }
                ) {
                    Text(stringResource(R.string.delete_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { deletePlantId = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun PlantListItem(
    plant: Plant,
    onItemSelect: (Plant) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Plant) -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onItemSelect(plant) }
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = plant.name.asString(),
                    modifier = Modifier.weight(1f),
                    style = typography.headlineSmall
                )
                TextButton(onClick = { onDeleteClick(plant) }) {
                    Text(
                        text = stringResource(R.string.delete_action),
                        style = typography.labelLarge
                    )
                }
            }
            Text(text = plant.type.asString(), style = typography.titleMedium)
            Text(text = plant.description.asString(), style = typography.titleMedium)
            Text(
                text = "${stringResource(R.string.water)} ${plant.schedule.asString()}",
                style = typography.titleMedium
            )
        }
    }
}

@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(R.string.five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, plantName),
        Reminder(R.string.one_day, ONE_DAY, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_week, SEVEN_DAYS, TimeUnit.DAYS, plantName),
        Reminder(R.string.one_month, THIRTY_DAYS, TimeUnit.DAYS, plantName)
    )
    val unitOptions = listOf(
        TimeUnit.SECONDS to R.string.unit_seconds,
        TimeUnit.MINUTES to R.string.unit_minutes,
        TimeUnit.HOURS to R.string.unit_hours,
        TimeUnit.DAYS to R.string.unit_days
    )

    val context = LocalContext.current
    var customDurationText by rememberSaveable { mutableStateOf("") }
    var selectedUnitIndex by rememberSaveable { mutableStateOf(1) } // Default: Minutes
    var showDurationError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDialogDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val value = customDurationText.toLongOrNull()
                    if (value == null || value <= 0L) {
                        showDurationError = true
                    } else {
                        val (unit, unitRes) = unitOptions[selectedUnitIndex]
                        onScheduleReminder(
                            Reminder(
                                duration = value,
                                unit = unit,
                                plantName = plantName,
                                customLabel = "$value ${context.getString(unitRes).lowercase()}"
                            )
                        )
                        onDialogDismiss()
                    }
                }
            ) {
                Text(stringResource(R.string.schedule_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.remind_me, plantName)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                reminders.forEach { reminder ->
                    val resId = reminder.durationRes ?: return@forEach
                    Text(
                        text = stringResource(resId),
                        modifier = Modifier
                            .clickable {
                                onScheduleReminder(reminder)
                                onDialogDismiss()
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = stringResource(R.string.custom_reminder_title),
                    style = typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customDurationText,
                    onValueChange = { input ->
                        customDurationText = input.filter { it.isDigit() }
                        showDurationError = false
                    },
                    label = { Text(stringResource(R.string.duration_label)) },
                    isError = showDurationError,
                    supportingText = if (showDurationError) {
                        { Text(stringResource(R.string.duration_invalid)) }
                    } else {
                        null
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    unitOptions.forEachIndexed { index, (_, labelRes) ->
                        val selected = index == selectedUnitIndex
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedUnitIndex = index },
                            shape = MaterialTheme.shapes.small,
                            color = if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline
                                }
                            )
                        ) {
                            Text(
                                text = stringResource(labelRes),
                                color = if (selected) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                style = typography.labelMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun AddPlantDialog(
    onDismiss: () -> Unit,
    onSavePlant: (name: String, type: String, description: String, schedule: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var schedule by rememberSaveable { mutableStateOf("") }
    var showNameError by rememberSaveable { mutableStateOf(false) }

    val defaultType = stringResource(R.string.default_plant_type)
    val defaultDescription = stringResource(R.string.default_plant_description)
    val defaultSchedule = stringResource(R.string.default_plant_schedule)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_plant_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showNameError = false
                    },
                    label = { Text(stringResource(R.string.plant_name_label)) },
                    isError = showNameError,
                    supportingText = if (showNameError) {
                        { Text(stringResource(R.string.plant_name_required)) }
                    } else {
                        null
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text(stringResource(R.string.plant_type_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.plant_description_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    label = { Text(stringResource(R.string.plant_schedule_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmedName = name.trim()
                    if (trimmedName.isEmpty()) {
                        showNameError = true
                    } else {
                        onSavePlant(
                            trimmedName,
                            type.trim().ifEmpty { defaultType },
                            description.trim().ifEmpty { defaultDescription },
                            schedule.trim().ifEmpty { defaultSchedule }
                        )
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PlantListItemPreview() {
    WaterMeTheme {
        PlantListItem(DataSource.plants[0], {})
    }
}

@Preview(showBackground = true)
@Composable
fun PlantListContentPreview() {
    WaterMeTheme {
        PlantListContent(plants = DataSource.plants, onScheduleReminder = {})
    }
}
