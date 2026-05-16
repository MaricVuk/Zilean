package com.example.zilean.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zilean.data.FoodEntry
import com.example.zilean.platformValue
import kotlinx.datetime.LocalDate
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable



@Composable
private fun animateDashboardInt(target: Int, label: String) = animateIntAsState(
    targetValue = target,
    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    label = label
)

@Composable
fun HealthDashboard(
    name: String?,
    currentCalories: Int,
    currentProtein: Int,
    currentCarb: Int,
    currentFat: Int,
    proteinGoal: Int,
    caloriesGoal: Int,
    carbsGoal: Int,
    fatsGoal: Int,
    foodList: List<FoodEntry>,
    onAddFoodClick: () -> Unit,
    onDeleteEntry: (FoodEntry) -> Unit,
    onScanClick: () -> Unit,
    onMenuClick: () -> Unit,
    onEditEntry: (FoodEntry) -> Unit,
    onSettingsClick: () -> Unit,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    waterGoal: Int,
    currentWater: Int,
    onAddWater: (Int) -> Unit,
    onWaterCardClick: () -> Unit
) {
    var editingEntry by remember { mutableStateOf<FoodEntry?>(null) }
    var showWaterDialog by remember { mutableStateOf(false) }

    if (showWaterDialog) {
        AddWaterDialog(
            onDismiss = { showWaterDialog = false },
            onConfirm = { amount ->
                onAddWater(amount)
                showWaterDialog = false
            }
        )
    }

    val animatedCalories by animateDashboardInt(currentCalories, "CaloriesAnimation")
    val animatedProtein by animateDashboardInt(currentProtein, "ProteinAnimation")
    val animatedCarbs by animateDashboardInt(currentCarb, "CarbsAnimation")
    val animatedFats by animateDashboardInt(currentFat, "FatsAnimation")

    val targetProgress = if (caloriesGoal > 0) currentCalories.toFloat() / caloriesGoal else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "ProgressAnimation"
    )

    val animatedPercentage by animateDashboardInt(
        if (caloriesGoal > 0) (currentCalories * 100) / caloriesGoal else 0,
        "PercentageAnimation"
    )

    editingEntry?.let { entry ->
        AddFoodDialog(
            initialName = entry.name,
            initialProtein = entry.protein.toString(),
            initialCalories = entry.calories.toString(),
            initialCarbs = entry.carbs.toString(),
            initialFats = entry.fats.toString(),
            showSavePresetOption = false,
            onDismiss = { editingEntry = null },
            onConfirm = { n, prot, cal, carb, fat, _ ->
                onEditEntry(entry.copy(name = n, protein = prot, calories = cal, carbs = carb, fats = fat))
                editingEntry = null
            }
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val actionButtonSize = maxWidth * 0.12f
        val waterCardHeight = (actionButtonSize * 2) + 12.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(112.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 10.dp,
                        trackColor = MaterialTheme.colorScheme.onSurface,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${animatedPercentage}%",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = platformValue(android = 28.sp, ios = 22.sp)
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column {
                    Text("Kalorije", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
                    Row (verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            text = animatedCalories.toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "/$caloriesGoal",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text("kcal", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MacroCard("Hidrati", animatedCarbs.toString(), carbsGoal.toString(), Color(0xFF3B82F6), Modifier.weight(1f))
            MacroCard("Proteini", animatedProtein.toString(), proteinGoal.toString(), Color(0xFFc40404), Modifier.weight(1f))
            MacroCard("Masti", animatedFats.toString(), fatsGoal.toString(), Color(0xFFFACC15), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WaterCard(
                modifier = Modifier.weight(1f).height(waterCardHeight), 
                currentWater = currentWater, 
                waterGoal = waterGoal, 
                onAddWater = { showWaterDialog = true },
                onCardClick = onWaterCardClick
            )

            Column(horizontalAlignment = Alignment.End) {
                Row(horizontalArrangement = Arrangement.End) {
                    DashboardActionButton(Icons.AutoMirrored.Filled.List, "Otvori jelovnik", actionButtonSize, onMenuClick)
                    Spacer(modifier = Modifier.width(12.dp))
                    DashboardActionButton(Icons.Default.Add, "Dodaj novo", actionButtonSize, onAddFoodClick)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.End) {
                    DashboardActionButton(Icons.Default.QrCodeScanner, "Skeniraj bar-kod", actionButtonSize, onScanClick)
                    Spacer(modifier = Modifier.width(12.dp))
                    DashboardActionButton(Icons.Default.Settings, "Settings", actionButtonSize, onSettingsClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        CalendarHeader(
            selectedDate = selectedDate,
            onDateChange = onDateChange,
            onPreviousDay = onPreviousDay,
            onNextDay = onNextDay,
        )

        //Spacer(modifier = Modifier.height(3.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            //contentPadding = PaddingValues(bottom = 12.dp),
        ) {
            items(foodList) { currentEntry ->
                FoodItemRow(
                    entry = currentEntry,
                    onDelete = { onDeleteEntry(currentEntry) },
                    onEdit = { editingEntry = currentEntry },
                )
            }
        }
    }
}
}

@Composable
private fun DashboardActionButton(
    imageVector: ImageVector,
    contentDescription: String,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    val iconSize = size * 0.8f

    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(size * 0.28f),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize),
            contentDescription = contentDescription
        )
    }
}

@Composable
fun WaterCard(
    currentWater: Int,
    waterGoal: Int,
    onAddWater: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedWater by animateDashboardInt(currentWater, "WaterAnimation")

    Card(
        modifier = modifier.fillMaxWidth().clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val h = maxHeight
            val buttonSize = h * 0.8f
            val iconSize = buttonSize * 0.7f
            
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Voda",
                        color = Color(0xFF34D399),
                        fontSize = (h.value * platformValue(android = 0.35f, ios = 0.3f)).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = animatedWater.toString(),
                            fontSize = (h.value * platformValue(android = 0.3f, ios = 0.25f)).sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " /$waterGoal",
                            fontSize = (h.value * platformValue(android = 0.2f, ios = 0.15f)).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onAddWater,
                    modifier = Modifier
                        .size(buttonSize)
                        .background(
                            color = Color(0xFF34D399).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(buttonSize / 2),
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = "Dodaj vodu",
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

@Composable
fun MacroCard(
    label: String,
    value: String,
    value2: String,
    color: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),//.height(82.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Center) {
            Text(
                text = label,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            //Spacer(modifier = Modifier.height(1.dp))

            Row (
                verticalAlignment = Alignment.Top,
                //horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "/$value2",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

