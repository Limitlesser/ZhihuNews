package wind.zhihunews.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.MenuItem
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


abstract class BaseActivity : RxAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(this@BaseActivity, Observer { observer(it!!) })
    }

}

abstract class ViewModel {

    protected val subject: Subject<Pair<String, Any>> = PublishSubject.create()

    fun <T> observable(init: T): ReadWriteProperty<Any?, T> =
            Delegates.observable(init) { property, _, value ->
                subject.onNext(property.name to value as Any)
            }

    @Suppress("UNCHECKED_CAST")
    fun <T> property(property: KProperty<T>): Observable<T> =
            subject.filter { (name, _) -> name == property.name }
                    .map { (_, value) -> value as T }


}
