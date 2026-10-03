    @file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
package com.ioslauncher.pro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // फोन के सारे ऐप्स लोड करना
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val allApps = packageManager.queryIntentActivities(mainIntent, 0).map {
            AppInfo(
                name = it.loadLabel(packageManager).toString(),
                packageName = it.activityInfo.packageName,
                icon = it.loadIcon(packageManager).toBitmap().asImageBitmap()
            )
        }.sortedBy { it.name.lowercase() }

        // नीचे डॉक के 4 मुख्य ऐप्स
        val dockApps = allApps.take(4)
        val homeApps = if (allApps.size > 4) allApps.drop(4) else allApps

        // हर पेज पर 20 ऐप्स (4 कॉलम x 5 पंक्तियाँ)
        val pages = homeApps.chunked(20)

        setContent {
            Ios18ProMaxHomeScreen(
                pages = pages,
                dockApps = dockApps,
                onAppClick = { packageName ->
                    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        startActivity(launchIntent)
                    }
                }
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // होम स्क्रीन खुली रहेगी
    }
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: androidx.compose.ui.graphics.ImageBitmap
)

@Composable
fun Ios18ProMaxHomeScreen(
    pages: List<List<AppInfo>>,
    dockApps: List<AppInfo>,
    onAppClick: (String) -> Unit
) {
    val totalPages = if (pages.isEmpty()) 1 else pages.size
    val pagerState = rememberPagerState(pageCount = { totalPages })

    // समय और तारीख
    val timeFormat = SimpleDateFormat("h:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
    val currentTime = remember { timeFormat.format(Date()) }
    val currentDate = remember { dateFormat.format(Date()) }

    // iPhone 18 Pro Max डीप ग्रेडिएंट बैकग्राउंड
    val iosWallpaper = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF070B19),
            Color(0xFF14122E),
            Color(0xFF26123D),
            Color(0xFF0F1A30),
            Color(0xFF050814)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(iosWallpaper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Dynamic Island
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(35.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = Color.Black)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF111827))
                    )
                    Text(
                        text = "18 Pro Max",
                        color = Color(0xFF60A5FA),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. iOS 18 विजेट्स (घड़ी + बैटरी)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0x28FFFFFF))
                        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(24.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                        Text(text = "CUPERTINO", color = Color(0xFFA1A1AA), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = currentTime, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = currentDate, color = Color(0xFFE4E4E7), fontSize = 11.sp, maxLines = 1)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0x28FFFFFF))
                        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(24.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "BATTERY", color = Color(0xFFA1A1AA), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF34D399))
                            )
                        }
                        Text(text = "85%", color = Color(0xFF34D399), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = "iQOO 5G • iOS 18", color = Color(0xFFE4E4E7), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. हॉरिजॉन्टल पेज स्वाइपिंग (बाएं-दाएं स्वाइप)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val currentApps = pages.getOrElse(pageIndex) { emptyList() }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val rows = currentApps.chunked(4)
                        for (rowApps in rows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (app in rowApps) {
                                    IosAppIcon(app = app, onClick = { onAppClick(app.packageName) })
                                }
                                val remaining = 4 - rowApps.size
                                repeat(remaining) {
                                    Spacer(modifier = Modifier.width(68.dp))
                                }
                            }
                        }
                    }
                }
            }

            // 4. पेज डॉट्स (Page Dots)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(totalPages) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else Color(0x66FFFFFF))
                    )
                }
            }

            // 5. नीचे फ्लोटिंग कांच का डॉक (iOS Floating Glass Dock)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(16.dp, RoundedCornerShape(34.dp), spotColor = Color(0x40000000))
                    .clip(RoundedCornerShape(34.dp))
                    .background(Color(0x38FFFFFF))
                    .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(34.dp))
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (dockApp in dockApps) {
                        IosDockIcon(app = dockApp, onClick = { onAppClick(dockApp.packageName) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun IosAppIcon(app: AppInfo, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color.Black)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22000000))
        ) {
            androidx.compose.foundation.Image(
                bitmap = app.icon,
                contentDescription = app.name,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = app.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun IosDockIcon(app: AppInfo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Color.Black)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        androidx.compose.foundation.Image(
            bitmap = app.icon,
            contentDescription = app.name,
            modifier = Modifier.fillMaxSize()
        )
    }
}               repeat(remaining) {
                                    Spacer(modifier = Modifier.width(68.dp))
                                }
                            }
                        }
                    }
                }
            }

            // 4. पेज इंडिकेटर डॉट्स (Page Dots)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(totalPages) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else Color(0x66FFFFFF))
                    )
                }
            }

            // 5. नीचे फ्लोटिंग कांच का डॉक (iOS Floating Glass Dock)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(16.dp, RoundedCornerShape(34.dp), spotColor = Color(0x40000000))
                    .clip(RoundedCornerShape(34.dp))
                    .background(Color(0x38FFFFFF))
                    .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(34.dp))
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (dockApp in dockApps) {
                        IosDockIcon(app = dockApp, onClick = { onAppClick(dockApp.packageName) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

// होम स्क्रीन ऐप आइकन (iOS Squircle Style)
@Composable
fun IosAppIcon(app: AppInfo, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color.Black)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22000000))
        ) {
            androidx.compose.foundation.Image(
                bitmap = app.icon,
                contentDescription = app.name,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = app.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

// बॉटम डॉक आइकन (बिना टेक्स्ट के केवल बड़ा सुंदर आइकन)
@Composable
fun IosDockIcon(app: AppInfo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Color.Black)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        androidx.compose.foundation.Image(
            bitmap = app.icon,
            contentDescription = app.name,
            modifier = Modifier.fillMaxSize()
        )
    }
}
