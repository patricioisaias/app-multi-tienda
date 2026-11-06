package com.example.multitienda.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Helper para el Recurso Nativo 2 (Rúbrica IE 2.4.1):
 * Manejo del motor háptico / vibrador físico del dispositivo,
 * compatible con Android 12+ (API 31+) y versiones anteriores.
 */
object HapticFeedbackHelper {

    fun vibrar(context: Context, duracionMs: Long = 100) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.let { vibrator ->
                    if (vibrator.hasVibrator()) {
                        val effect = VibrationEffect.createOneShot(duracionMs, VibrationEffect.DEFAULT_AMPLITUDE)
                        vibrator.vibrate(effect)
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (it.hasVibrator()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val effect = VibrationEffect.createOneShot(duracionMs, VibrationEffect.DEFAULT_AMPLITUDE)
                            it.vibrate(effect)
                        } else {
                            @Suppress("DEPRECATION")
                            it.vibrate(duracionMs)
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Manejo seguro en emuladores o entornos sin motor háptico
        }
    }

    fun vibrarExito(context: Context) {
        vibrar(context, 80)
    }

    fun vibrarAlerta(context: Context) {
        vibrar(context, 200)
    }

    fun vibrarDoblePulso(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 100, 80, 150)
                val amplitudes = intArrayOf(0, 200, 0, 255)
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vm?.defaultVibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    v?.vibrate(effect)
                }
            } else {
                vibrar(context, 150)
            }
        } catch (_: Exception) {
            vibrar(context, 150)
        }
    }
}

