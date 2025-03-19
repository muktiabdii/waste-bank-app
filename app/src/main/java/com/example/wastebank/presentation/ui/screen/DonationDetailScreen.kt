package com.example.wastebank.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wastebank.R
import com.example.wastebank.presentation.ui.component.*
import com.example.wastebank.presentation.ui.theme.BrownMain
import com.example.wastebank.presentation.ui.theme.Typography
import com.example.wastebank.presentation.viewmodel.DonationViewModel

@Composable
fun DonationDetailScreen(navController: NavController, donationViewModel: DonationViewModel) {
    var selectedNominal by remember { mutableStateOf<Int?>(null) }
    var customNominal by remember { mutableStateOf("") }

    val selectedDonation by donationViewModel.selectedDonation.collectAsState()

    var showDialogUpload by remember { mutableStateOf(false) }
    var showPopUpNotif by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val nominalList = listOf(10000, 25000, 50000, 100000, 250000, 500000)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))

            // Header dengan tombol kembali
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Detail Donasi",
                    style = Typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        item {
            // Card detail donasi
            CardDonationDetail(
                donation = selectedDonation
            )
        }

        item {
            Text(
                text = "Pilih Nominal Donasi",
                style = Typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(nominalList) { nominal ->
                    CardNominal(
                        nominal = nominal,
                        isSelected = selectedNominal == nominal,
                        onClick = {
                            selectedNominal = nominal
                            customNominal = ""
                        }
                    )
                }
            }
        }

        item {
            Text(
                text = "Atau Masukkan Nominal Lain",
                style = Typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        item {
            TextFieldNominal(
                value = customNominal,
                onValueChange = {
                    customNominal = it
                    selectedNominal = null
                }
            )
        }

        item {
            Text(
                text = "Informasi Transfer",
                style = Typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        item {
            CardInfoTransfer(
                donation = selectedDonation,
                totalAmount = selectedNominal ?: customNominal.toIntOrNull() ?: 0
            )
        }

        item {
            // Button Salin Nomor Rekening
            ButtonAuth(
                text = "SALIN NOMOR REKENING",
                backgroundColor = Color.White,
                textColor = BrownMain,
                borderColor = BrownMain,
                onClick = {
                    val accountNumber = selectedDonation?.accountNumber.orEmpty()
                    if (accountNumber.isNotEmpty()) {
                        clipboardManager.setText(AnnotatedString(accountNumber))
                        Toast.makeText(context, "Nomor rekening disalin!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }

        item {
            // Button Upload Bukti Transfer
            ButtonAuth(
                text = "UPLOAD BUKTI TRANSFER",
                backgroundColor = BrownMain,
                textColor = Color.White,
                onClick = { showDialogUpload = true }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog Upload Bukti Transfer
    if (showDialogUpload) {
        DialogUpload(
            subtotal = selectedNominal ?: customNominal.toIntOrNull() ?: 0,
            pengiriman = 0,
            onDismiss = { showDialogUpload = false },
            onUploadClick = { showPopUpNotif = true }
        )
    }

    // PopUp Notifikasi Pembayaran akan Diproses
    if (showPopUpNotif) {
        PopUpNotif(
            iconResId = R.drawable.ic_success,
            message = "Pembayaran akan Diproses!",
            buttonText = "KEMBALI",
            navController = navController,
            onDismiss = {
                showPopUpNotif = false
                showDialogUpload = false
                navController.navigate("donate_screen") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                }
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDonationDetailScreen() {
//    val navController = rememberNavController()
//    DonationDetailScreen(navController)
//}