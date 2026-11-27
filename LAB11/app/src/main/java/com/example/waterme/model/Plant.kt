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

package com.example.waterme.model

import androidx.annotation.StringRes

/**
 * Represents either a string resource ID or a user-entered plain string.
 */
sealed interface PlantText {
    data class Resource(@param:StringRes val resId: Int) : PlantText
    data class Plain(val value: String) : PlantText
}

/**
 * Data class for representing the information required for each plant card.
 */
data class Plant(
    val id: String,
    val name: PlantText,
    val type: PlantText,
    val description: PlantText,
    val schedule: PlantText
) {
    constructor(
        id: String,
        @StringRes name: Int,
        @StringRes type: Int,
        @StringRes description: Int,
        @StringRes schedule: Int
    ) : this(
        id = id,
        name = PlantText.Resource(name),
        type = PlantText.Resource(type),
        description = PlantText.Resource(description),
        schedule = PlantText.Resource(schedule)
    )
}
