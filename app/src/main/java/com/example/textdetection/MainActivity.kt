package com.example.textdetection


import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.textdetection.ui.theme.TextDetectionTheme
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextDetectionTheme {
                TextDetectionScreen()
            }
        }
    }
}

/**
 * photoPicker 앱을 사용해서 사진을 가지고 온다
 * 1. phtoPicker 관련 설정
 * 2. 이미지 불러오기 버튼
 * 3. 앱 화면 : 버튼, 인식된 텍스트
 * mlkit을 사용해서 텍스트를 인식한다
 *
 */


@Composable
fun TextDetectionScreen(modifier: Modifier = Modifier) {

    // When using Latin script library
    val defaultRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    // When using Korean script library
    val krRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    // 이미지 선택을 위한 미디어 런처 초기화
    val pickMultipleMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            selectedImageUri = uri
        } else {
            Log.d("PhotoPicker", "No photos selected")
        }
    }

    // 이미지만 선택할 수 있도록 설정
    val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

    // Composable에서 버튼 추가 (UI 요소)
    Column(modifier = modifier.padding(48.dp)) {
        Button(
            onClick = { pickMultipleMedia.launch(request) }) {
            Text("Select Photos")
        }

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "selected image",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TextDetectionTheme {
        TextDetectionScreen()
    }
}