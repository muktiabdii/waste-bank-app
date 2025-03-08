package com.example.wastebank.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wastebank.R
import com.example.wastebank.domain.model.Product
import com.example.wastebank.domain.model.ProductCategory
import com.example.wastebank.presentation.ui.component.*
import com.example.wastebank.presentation.ui.theme.GreenBg
import com.example.wastebank.presentation.ui.theme.Typography
import com.example.wastebank.presentation.ui.theme.YellowMain

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowMain)
    ) {
        // topbar
        TopBar(username = "Raion", points = 2450)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(22.dp))

                // card poin
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CardPoint(
                        points = 2540,
                        onViewPointsClick = { },
                        onRedeemPointsClick = { }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // misi kamu hari ini
                Text(
                    text = "Misi Kamu Hari Ini",
                    style = Typography.headlineLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // card kampanye bulan maret
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CardCampaign(
                        monthName = "Maret",
                        daysLeft = 25,
                        completedMissions = 3,
                        totalMissions = 30
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // card misi harian
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CardMission(
                        missionTitle = "Misi Harian",
                        missionDescription = "Kumpulin 10 botol plastik",
                        ptsReward = 100,
                        currentProgress = 8,
                        totalProgress = 10,
                        progressSuffix = "botol sudah terkumpul"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // produk menarik buat kamu
                Text(
                    text = "Produk Menarik Buat Kamu",
                    style = Typography.headlineLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // semua produk
                val products = listOf(
                    Product("Jeans Totebag", ProductCategory.FASHION, "Rp25.000", R.drawable.logo),
                    Product("Pot Bunga Hewan", ProductCategory.VASE, "Rp20.000", R.drawable.logo),
                    Product("Lampu Sendok", ProductCategory.CRAFT, "Rp15.000", R.drawable.logo)
                )

                // looping list produk
                LazyRow(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                ) {
                    items(products) { product ->
                        CardProduct(
                            productImageResId = product.imageRes,
                            productName = product.name,
                            productCategory = product.category.toString(),
                            productPrice = product.price,
                            onClick = { },
                            onAddToCart = { }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            BottomNavigation(navController = navController)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
