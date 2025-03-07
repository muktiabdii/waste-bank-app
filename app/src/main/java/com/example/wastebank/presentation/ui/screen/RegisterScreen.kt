package com.example.wastebank.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wastebank.R
import com.example.wastebank.presentation.ui.component.ButtonAuth
import com.example.wastebank.presentation.ui.component.TextFieldAuth
import com.example.wastebank.presentation.ui.component.TextFieldPassword
import com.example.wastebank.presentation.ui.theme.BrownMain
import com.example.wastebank.presentation.ui.theme.GreenBg
import com.example.wastebank.presentation.ui.theme.GreyMedium
import com.example.wastebank.presentation.ui.theme.TextRed
import com.example.wastebank.presentation.ui.theme.manrope
import com.example.wastebank.presentation.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    var namaState by remember { mutableStateOf("") }
    var teleponState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var sandiState by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenBg)
    ) {
        // Tombol Back
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(30.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "back"
            )
        }

        // Dekorasi atas
        Image(
            painter = painterResource(id = R.drawable.decor_top),
            contentDescription = "decoration",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 10.dp)
        )

        // Dekorasi bawah
        Image(
            painter = painterResource(id = R.drawable.decor_lower),
            contentDescription = "decoration",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .scale(1.15f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(44.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Bergabung dengan SABI",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Jadilah bagian dari gerakan SABI dalam mengelola sampah organik, berkolaborasi dengan sesama peduli lingkungan, dan raih berbagai reward menarik dari kontribusimu.",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                    textAlign = TextAlign.Justify,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // TextFields Nama
            Text(
                text = "Nama",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextFieldAuth(
                value = namaState,
                placeholder = "Masukkan nama Anda",
                onValueChange = {
                    namaState = it
                })
            Spacer(modifier = Modifier.height(8.dp))

            // TextFields No. Telepon
            Text(
                text = "No. Telepon",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                    textAlign = TextAlign.Start,
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextFieldAuth(
                value = teleponState,
                placeholder = "Masukkan nomor telepon",
                onValueChange = {
                    val regex = Regex("^\\+?\\d{0,15}$")
                    if (regex.matches(it)) {
                        teleponState = it
                    }
                })
            Spacer(modifier = Modifier.height(8.dp))

            // TextFields Email
            Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                    textAlign = TextAlign.Start,
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextFieldAuth(
                value = emailState,
                placeholder = "Masukkan email",
                onValueChange = {
                    emailState = it
                })
            Spacer(modifier = Modifier.height(8.dp))

            // TextFields kata sandi
            Text(
                text = "Kata Sandi",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                    textAlign = TextAlign.Start,
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextFieldPassword(
                value = sandiState,
                placeholder = "Minimal 8 karakter",
                onValueChange = {
                    sandiState = it
                },
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Gender Selection
            Text(
                text = "Gender",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = manrope,
                    fontWeight = FontWeight.Normal,
                    color = GreyMedium,
                )
            )
            Row {
                var selectedGender by remember {
                    mutableStateOf("")
                }
                GenderRadioButton("Wanita", selectedGender) {
                    selectedGender = it
                }
                Spacer(modifier = Modifier.width(5.dp))
                GenderRadioButton("Pria", selectedGender) {
                    selectedGender = it
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Button Daftar
            ButtonAuth(
                text = "DAFTAR",
                onClick = { navController.navigate("user_login_screen") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Sudah Punya Akun
            LoginText(navController)
        }
    }
}

// Radio Button Gender
@Composable
fun GenderRadioButton(label: String, selected: String, onSelect: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == label,
            onClick = { onSelect(label) },
            colors = RadioButtonDefaults.colors(
                selectedColor = BrownMain,
                unselectedColor = GreyMedium
            )
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

// Button Masuk
@Composable
fun LoginText(navController: NavController) {
    val annotatedText = buildAnnotatedString {
        append("Sudah Punya Akun? ")

        pushStringAnnotation(tag = "login", annotation = "login")
        withStyle(
            style = SpanStyle(color = TextRed)
        ) {
            append("Masuk")
        }
        pop()
    }

    Text(
        text = annotatedText,
        fontSize = 12.sp,
        color = GreyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("user_login_screen") }
    )
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}