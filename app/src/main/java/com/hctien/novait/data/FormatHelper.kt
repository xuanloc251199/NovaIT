package com.hctien.novait.data

import android.annotation.SuppressLint

class FormatHelper {
    @SuppressLint("DefaultLocale")
    fun formatViewCount(viewCount: Int): String {
        return when {
            viewCount >= 1_000_000 -> String.format("%.1f Tr", viewCount / 1_000_000f)
            viewCount >= 1_000 -> String.format("%.1f K", viewCount / 1_000f)
            else -> viewCount.toString()
        }
    }
}