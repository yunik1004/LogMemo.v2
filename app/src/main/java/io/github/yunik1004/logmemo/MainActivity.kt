package io.github.yunik1004.logmemo

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {
    private lateinit var mainUI: MainActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUI = MainActivityUi()
        mainUI.setContentView(this)
    }
}

class MainActivityUi : AnkoComponent<MainActivity> {
    private lateinit var memo: EditText

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            padding = dip(8)

            // Setting button
            floatingActionButton {
                imageResource = R.drawable.ic_more_vert_black_24dp
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                compatElevation = 0.0f
                onClick {
                    toast("Settings").show()
                }
            }.lparams {
                gravity = Gravity.END
            }

            // Color-change button
            floatingActionButton {
                imageResource = R.drawable.ic_invert_colors_black_24dp
                backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                compatElevation = 0.0f
                onClick {
                    toast("Change color").show()
                }
            }.lparams {
                gravity = Gravity.END
            }

            recyclerView {

            }.lparams(width = matchParent, height = wrapContent, weight = 1.0f){}

            linearLayout {
                // Memo input
                memo = editText {
                    id = R.id.memo_edit_text
                    hintResource = R.string.memo_write_hint
                    maxLines = 5
                }.lparams(width = wrapContent, height = wrapContent, weight = 1.0f){}

                // Memo save button
                button(R.string.memo_save) {
                    onClick {
                        toast(getMemo()).show()
                    }
                }.lparams(width = wrapContent, height = wrapContent){
                    gravity = Gravity.BOTTOM
                }
            }.lparams(width = matchParent, height = wrapContent){}
        }
    }

    private fun getMemo(): String {
        return memo.text.toString()
    }
}
