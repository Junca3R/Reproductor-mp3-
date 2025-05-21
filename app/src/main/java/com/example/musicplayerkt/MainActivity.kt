package com.example.musicplayerkt

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.*
import com.example.musicplayerkt.ui.theme.MusicPlayerktTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerktTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MusicPlayerUI()
                }
            }
        }
    }
}

@Composable
fun MusicPlayerUI() {
    val context = LocalContext.current

    val songs = listOf(
        R.raw.song1,
        R.raw.song2,
        R.raw.song3,
        R.raw.song4,
        R.raw.song5
    )

    var currentSongIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    // Creamos el MediaPlayer y lo reiniciamos cuando cambia la canción o isPlaying
    val mediaPlayer = remember(currentSongIndex) {
        MediaPlayer.create(context, songs[currentSongIndex]).apply {
            setOnCompletionListener {
                // Cuando termina la canción, pasamos a la siguiente y seguimos reproduciendo
                currentSongIndex = (currentSongIndex + 1) % songs.size
            }
        }
    }

    // Este efecto escucha cambios en isPlaying y currentSongIndex para controlar reproducción
    LaunchedEffect(isPlaying, currentSongIndex) {
        if (isPlaying) {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        } else {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }
    }

    // Liberar el MediaPlayer anterior cuando cambia la canción
    DisposableEffect(currentSongIndex) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.imgback),
            contentDescription = "Imagen de canción",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🎵 Reproductor de Música",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.White,
                fontSize = 30.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    currentSongIndex = if (currentSongIndex - 1 < 0) songs.size - 1 else currentSongIndex - 1
                    isPlaying = true // Aseguramos que la canción se reproduzca automáticamente
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.SkipPrevious,
                        contentDescription = "Retroceder",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(onClick = {
                    isPlaying = !isPlaying
                }) {
                    Icon(
                        imageVector = if (isPlaying)
                            androidx.compose.material.icons.Icons.Default.Pause
                        else
                            androidx.compose.material.icons.Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }

                IconButton(onClick = {
                    currentSongIndex = (currentSongIndex + 1) % songs.size
                    isPlaying = true // También aquí para que se reproduzca automáticamente
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.SkipNext,
                        contentDescription = "Avanzar",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

