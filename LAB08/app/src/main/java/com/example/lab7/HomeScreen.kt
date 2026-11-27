package com.example.lab7

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val yellowBg = Color(0xFFF6BD38)
    val primaryRed = Color(0xFFB71C1C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(yellowBg)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            // App Title: PIZZERIA
            Text(
                text = "P I Z Z E R I A",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = primaryRed,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle
            Text(
                text = "Delivering\nDeliciousness right\nto your door!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Dot Slider Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                )
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.Black)
                )
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // START ORDER Button
            Button(
                onClick = { /* Start order action */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                modifier = Modifier
                    .width(180.dp)
                    .height(46.dp)
            ) {
                Text(
                    text = "START ORDER",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SignOut Button
            Button(
                onClick = {
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.signOut()
                    navController.navigate(Screen.Signin.rout) {
                        popUpTo(0)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                modifier = Modifier
                    .width(140.dp)
                    .height(44.dp)
            ) {
                Text(
                    text = "SignOut",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery Guy Graphic Image at Bottom
        Image(
            painter = painterResource(id = R.drawable.delivery_guy),
            contentDescription = "Delivery Guy",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.FillWidth
        )
    }
}
