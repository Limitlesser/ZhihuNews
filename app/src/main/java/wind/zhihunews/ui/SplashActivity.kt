package wind.zhihunews.ui

import android.app.ActivityOptions
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.jakewharton.rxbinding2.widget.text
import wind.zhihunews.base.BaseActivity
import wind.zhihunews.base.ViewModel
import wind.zhihunews.service.NewsService
import wind.zhihunews.widget.bind
import wind.zhihunews.widget.imageUri
import wind.zhihunews.widget.simpleDraweeView
import org.jetbrains.anko.*
import wind.zhihunews.R
import wind.zhihunews.net.api


class SplashActivity : BaseActivity() {

    private lateinit var ui: SplashUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = SplashUI()
        ui.setContentView(this)
        ui.image.animate().apply {
            scaleX(1.1f);scaleY(1.1f)
            duration = 1000;startDelay = 1000
            withEndAction {
                startActivity(intentFor<MainActivity>(),
                        ActivityOptions.makeCustomAnimation(this@SplashActivity,
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle())
                finish()
            }
        }
    }
}


class SplashUI() : AnkoComponent<SplashActivity> {

    lateinit var image: ImageView

    override fun createView(ui: AnkoContext<SplashActivity>): View {
        return ui.apply {
            frameLayout {
                image = imageView {
                    imageResource = R.drawable.splash
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(matchParent, matchParent)
                textView {
                    textResource = R.string.app_name
                    textColor = Color.WHITE
                    typeface = Typeface.create(null as String?, Typeface.BOLD)
                    textSize = 22f
                }.lparams {
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                    bottomMargin = dip(30)
                }
            }
        }.view
    }

}