package daniellopes.io.newsappstarter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout().root)
        onInject()
    }

    protected abstract fun getLayout(): ViewBinding

    protected abstract fun onInject()
}