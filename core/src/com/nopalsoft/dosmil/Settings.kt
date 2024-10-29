package com.nopalsoft.dosmil

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val pref: Preferences = Gdx.app
        .getPreferences("com.tiar.dosmil")
    @JvmField
    var isMusicOn: Boolean = false
    @JvmField
    var isSoundOn: Boolean = false
    var didBuyNoAds: Boolean = false
    @JvmField
    var numberTimesPlayed: Int = 0
    @JvmField
    var bestScore: Long = 0

    @JvmStatic
    fun load() {
        bestScore = pref.getLong("bestScore", 0)
        numberTimesPlayed = pref.getInteger("numeroVecesJugadas", 0)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)
    }

    fun save() {
        pref.putLong("bestScore", bestScore)
        pref.putInteger("numeroVecesJugadas", numberTimesPlayed)
        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)
        pref.flush()
    }

    @JvmStatic
    fun setBestScores(score: Long) {
        if (bestScore < score) bestScore = score
        save()
    }
}
