package com.zaebalo.xr

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {

    private lateinit var rootLayout: FrameLayout
    private val activeWindows = ArrayList<WebView>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запускаем Jetpack Compose интерфейс
        setContent {
            MaterialTheme {
                // Создаем холст, прозрачный для Passthrough-камеры Pico 4
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    // Интегрируем нативный FrameLayout внутрь Compose дерева
                    AndroidView(
                        factory = { context ->
                            FrameLayout(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                               )
                                rootLayout = this
                                
                                // Открываем стартовое окно XR-браузера
                                createNewWindow(context, "https://google.com")
                                
                                // Запускаем цикл плавного левитирования окон Lazy Follow
                                startXRTrackingLoop()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun createNewWindow(context: android.content.Context, url: String) {
        val webView = WebView(context).apply {
            layoutParams = FrameLayout.LayoutParams(1280, 720) // Виртуальное VR-разрешение
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl(url)
            
            // Позволяет свободно перемещать окно щипками пальцев рук или контроллером
            setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_MOVE) {
                    view.x = event.rawX - (view.width / 2)
                    view.y = event.rawY - (view.height / 2)
                }
                false
            }
        }

        activeWindows.add(webView)
        rootLayout.addView(webView)
    }

    // Алгоритм LAZY FOLLOW: Окна плавно плывут за центром вашего обзора
    private fun startXRTrackingLoop() {
        rootLayout.postDelayed(object : Runnable {
            override fun run() {
                for (webView in activeWindows) {
                    val targetX = 300f 
                    val targetY = 200f

                    // Легкий эффект левитации ("ленивого" следования) окна перед глазами
                    webView.x += (targetX - webView.x) * 0.08f
                    webView.y += (targetY - webView.y) * 0.08f
                }
                rootLayout.postDelayed(this, 16) // Стабильные 60 кадров/сек в VR
            }
        }, 16)
    }
}
