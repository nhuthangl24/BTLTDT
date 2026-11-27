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

import com.example.waterme.R
import com.example.waterme.model.Plant

object DataSource {
    val plants = listOf(
        Plant(
            id = "preset_lithop",
            name = R.string.lithop,
            schedule = R.string.monthly,
            type = R.string.succulent,
            description = R.string.stone_mimicking_succulent
        ),
        Plant(
            id = "preset_carrot",
            name = R.string.carrot,
            schedule = R.string.daily,
            type = R.string.root,
            description = R.string.hardy_root_vegetable
        ),
        Plant(
            id = "preset_peony",
            name = R.string.peony,
            schedule = R.string.weekly,
            type = R.string.flower,
            description = R.string.spring_blooming_flower
        ),
        Plant(
            id = "preset_pothos",
            name = R.string.pothos,
            schedule = R.string.weekly,
            type = R.string.houseplant,
            description = R.string.indoor_vine
        ),
        Plant(
            id = "preset_fiddle_leaf_fig",
            name = R.string.fiddle_leaf_fig,
            schedule = R.string.weekly,
            type = R.string.broadleaf_evergreen,
            description = R.string.ornamental_fig
        ),
        Plant(
            id = "preset_strawberry",
            name = R.string.strawberry,
            schedule = R.string.daily,
            type = R.string.fruit,
            description = R.string.delicious_multiple_fruit
        )
    )
}
