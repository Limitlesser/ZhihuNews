package wind.zhihunews.widget

import android.graphics.drawable.ColorDrawable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import org.jetbrains.anko.backgroundResource
import wind.zhihunews.R


inline fun <T : View> T.applyStyle(block: T.() -> Unit) = block()

val baseToolBar: Toolbar.() -> Unit = {
    backgroundResource = R.color.colorPrimary
    popupTheme = R.style.ThemeOverlay_AppCompat_Light
}

val navigateToolbar: Toolbar.() -> Unit = {
    baseToolBar()
    contentInsetStartWithNavigation = 0
}

val collapsingToolbar: CollapsingToolbarLayout.() -> Unit = {
    setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Title)
    setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Title)
    contentScrim = ColorDrawable(context.attrColor(R.attr.colorPrimary))
    statusBarScrim = ColorDrawable(context.attrColor(R.attr.colorPrimary))
}