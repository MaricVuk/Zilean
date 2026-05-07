package com.example.zilean

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zilean.ui.theme.ZileanTheme

@Composable
fun HealthDashboard(proteinGoal: Int,
                    caloriesGoal: Int,
                    carbsGoal: Int,
                    fatsGoal: Int
                    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = 0.65f,
                        modifier = Modifier.size(100.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 8.dp,
                        trackColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "65%",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column {
                    Text("Preostalo", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    Row (verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            "0",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "/" + caloriesGoal.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal

                        )
                    }
                    Text("kcal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroCard(
                label = "Hidrati",
                value = "0",
                value2 = carbsGoal.toString() + "g",
                //icon = Icons.Default.WaterDrop,
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Proteini",
                value = "0",
                value2 = proteinGoal.toString() + "g",
                //icon = Icons.Default.Star,
                color = Color(0xFFc40404),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Masti",
                value = "0",
                value2 = fatsGoal.toString(),
                color = Color(0xFFFACC15),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        /*Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("DODAJ OBROK", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface)
        }*/
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
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            //Divider(color = color, thickness = 3.dp, modifier = Modifier.width(20.dp))

            //Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row (
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()

                )
                Text(
                    text = "/" + value2,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.alignByBaseline()

                )
            }
        }
    }
}

/*@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DashboardPreview() {
    HealthDashboard()
}*/


@Preview(name = "Light Mode", showBackground = true)
@Composable
fun DashboardLightPreview() {
    ZileanTheme(darkTheme = false) {
        //HealthDashboard()
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardDarkPreview() {
    ZileanTheme(darkTheme = true) {
        //HealthDashboard()
    }
}