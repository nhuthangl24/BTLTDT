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

package com.example.waterme.data

import android.content.Context
import androidx.core.content.edit
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.waterme.model.Plant
import com.example.waterme.model.PlantText
import com.example.waterme.worker.WaterReminderWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

class WorkManagerWaterRepository(context: Context) : WaterRepository {
    private val workManager = WorkManager.getInstance(context)
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val customPlants = loadCustomPlants().toMutableList()
    private val hiddenPresetIds = loadHiddenPresetIds().toMutableSet()

    private val _plants = MutableStateFlow(buildPlantList())
    override val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    override fun addPlant(name: String, type: String, description: String, schedule: String) {
        val newPlant = Plant(
            id = "custom_${UUID.randomUUID()}",
            name = PlantText.Plain(name),
            type = PlantText.Plain(type),
            description = PlantText.Plain(description),
            schedule = PlantText.Plain(schedule)
        )
        customPlants.add(0, newPlant)
        saveCustomPlants()
        _plants.value = buildPlantList()
    }

    override fun deletePlant(plant: Plant) {
        val removedCustom = customPlants.removeAll { it.id == plant.id }
        if (removedCustom) {
            saveCustomPlants()
        } else {
            hiddenPresetIds.add(plant.id)
            saveHiddenPresetIds()
        }
        _plants.value = buildPlantList()
    }

    override fun scheduleReminder(duration: Long, unit: TimeUnit, plantName: String) {
        val data = Data.Builder()
        data.putString(WaterReminderWorker.nameKey, plantName)

        val workRequestBuilder = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(duration, unit)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            "${plantName}_${duration}_${unit.name}",
            ExistingWorkPolicy.REPLACE,
            workRequestBuilder
        )
    }

    private fun buildPlantList(): List<Plant> {
        val visiblePresets = DataSource.plants.filterNot { it.id in hiddenPresetIds }
        return customPlants + visiblePresets
    }

    private fun loadCustomPlants(): List<Plant> {
        val raw = prefs.getString(KEY_CUSTOM_PLANTS, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            List(array.length()) { index ->
                val obj = array.getJSONObject(index)
                Plant(
                    id = obj.optString("id").ifEmpty { "custom_$index" },
                    name = PlantText.Plain(obj.getString("name")),
                    type = PlantText.Plain(obj.getString("type")),
                    description = PlantText.Plain(obj.getString("description")),
                    schedule = PlantText.Plain(obj.getString("schedule"))
                )
            }
        }.getOrDefault(emptyList())
    }

    private fun saveCustomPlants() {
        val array = JSONArray()
        customPlants.forEach { plant ->
            val obj = JSONObject().apply {
                put("id", plant.id)
                put("name", (plant.name as PlantText.Plain).value)
                put("type", (plant.type as PlantText.Plain).value)
                put("description", (plant.description as PlantText.Plain).value)
                put("schedule", (plant.schedule as PlantText.Plain).value)
            }
            array.put(obj)
        }
        prefs.edit { putString(KEY_CUSTOM_PLANTS, array.toString()) }
    }

    private fun loadHiddenPresetIds(): Set<String> =
        prefs.getStringSet(KEY_HIDDEN_PRESET_IDS, emptySet()) ?: emptySet()

    private fun saveHiddenPresetIds() {
        prefs.edit { putStringSet(KEY_HIDDEN_PRESET_IDS, hiddenPresetIds.toSet()) }
    }

    companion object {
        private const val PREFS_NAME = "waterme_plants"
        private const val KEY_CUSTOM_PLANTS = "custom_plants"
        private const val KEY_HIDDEN_PRESET_IDS = "hidden_preset_ids"
    }
}
