package io.github.yunik1004.logmemo

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)
    }
}

class MainActivityUi : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            padding = dip(8)

            floatingActionButton {
                backgroundColor = Color.BLUE
                imageResource = R.drawable.ic_invert_colors_black_24dp
            }

            recyclerView {

            }.lparams(width = matchParent, height = wrapContent, weight = 1.0f){}

            linearLayout {
                // Memo input
                val name = editText {
                    hintResource = R.string.memo_write_hint
                    maxLines = 5
                }.lparams(width = wrapContent, height = wrapContent, weight = 1.0f){}

                // Memo save button
                button(R.string.memo_save) {
                    onClick {
                        toast(name.text.toString()).show()
                    }
                }.lparams(width = wrapContent, height = wrapContent){
                    gravity = Gravity.BOTTOM
                }
            }.lparams(width = matchParent, height = wrapContent){}
        }
    }
}
