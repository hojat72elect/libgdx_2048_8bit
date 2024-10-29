package com.nopalsoft.dosmil.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.dosmil.MainGame
import com.nopalsoft.dosmil.handlers.GoogleGameServicesHandler
import com.nopalsoft.dosmil.handlers.RequestHandler

fun main(args: Array<String>) {

    val reqHandler = object : RequestHandler {
        override fun showRater() {}

        override fun loadInterstitial() {}

        override fun showInterstitial() {}

        override fun showFacebook() {}

        override fun showAdBanner() {}

        override fun shareOnTwitter(message: String) {}

        override fun shareOnFacebook(message: String) {}

        override fun removeAds() {}

        override fun hideAdBanner() {}
    }

    val gameHandler = object : GoogleGameServicesHandler {
        override fun signIn() {}

        override fun isSignedIn() = false

        override fun getLeaderboard() {}

        override fun submitScore(score: Long) {}
    }

    val cfg = LwjglApplicationConfiguration()
    cfg.title = "2048 8Bit"
    cfg.width = 480
    cfg.height = 800

    LwjglApplication(MainGame(reqHandler, gameHandler), cfg)

}