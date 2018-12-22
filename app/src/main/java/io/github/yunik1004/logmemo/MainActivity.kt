package io.github.yunik1004.logmemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createView()
    }

    private fun createView() {
        verticalLayout {
            padding = dip(8)

            val name: EditText = editText {
                hint = getString(R.string.memo_write_hint)
            }

            button(getString(R.string.memo_save)) {
                onClick {
                    toast(name.text.toString()).show()
                }
            }
        }
    }
}
