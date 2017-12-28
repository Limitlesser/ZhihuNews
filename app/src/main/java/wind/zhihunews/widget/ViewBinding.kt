package wind.zhihunews.widget

import com.facebook.drawee.view.SimpleDraweeView
import io.reactivex.Observable
import io.reactivex.functions.Consumer


infix fun <T> Consumer<in T>.bind(observable: Observable<T>) {
    observable.subscribe(this)
}

fun SimpleDraweeView.imageUri() = Consumer { uri: String -> setImageURI(uri) }