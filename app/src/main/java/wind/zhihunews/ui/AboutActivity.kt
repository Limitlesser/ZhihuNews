package wind.zhihunews.ui

import android.os.Bundle
import android.text.util.Linkify
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import wind.zhihunews.BuildConfig
import wind.zhihunews.R
import wind.zhihunews.base.BaseActivity
import wind.zhihunews.widget.attrDimen
import wind.zhihunews.widget.navigateToolbar


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AboutUI().setContentView(this)
    }
}

class AboutUI : AnkoComponent<AboutActivity> {
    override fun createView(ui: AnkoContext<AboutActivity>): View {
        return ui.owner.run {
            verticalLayout {
                gravity = Gravity.CENTER_HORIZONTAL
                toolbar(navigateToolbar)
                        .lparams(matchParent, attrDimen(R.attr.actionBarSize))
                        .apply {
                            setSupportActionBar(this)
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        }
                imageView(R.mipmap.ic_launcher)
                        .lparams(dip(80), dip(80)) {
                            topMargin = dip(50)
                        }
                textView(getString(R.string.app_name) + "V${BuildConfig.VERSION_NAME}") {
                    textSize = 18f
                }.lparams(wrapContent, wrapContent) {
                    topMargin = dip(20)
                }
                textView(R.string.declaration) {
                    autoLinkMask = Linkify.EMAIL_ADDRESSES or Linkify.WEB_URLS
                    setLineSpacing(dip(2).toFloat(), lineSpacingMultiplier)
                }.lparams(wrapContent, wrapContent) {
                    margin = dip(20)
                }
                textView((R.string.power_by))
                        .lparams(wrapContent, wrapContent) {
                            topMargin = dip(20)
                        }
            }
        }
    }

}