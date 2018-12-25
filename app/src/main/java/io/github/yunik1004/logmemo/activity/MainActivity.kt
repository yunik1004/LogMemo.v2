package io.github.yunik1004.logmemo.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.EditText
import io.github.yunik1004.logmemo.R
import io.github.yunik1004.logmemo.adapter.MemoAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
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
    private lateinit var memoEditText: EditText
    private lateinit var memoRecyclerView: RecyclerView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            padding = dip(8)

            // Layout
            verticalLayout {
                // Memo logs
                memoRecyclerView = themedRecyclerView(R.style.ScrollbarRecyclerView) {
                    val mLinearLayoutManager = LinearLayoutManager(context)
                    mLinearLayoutManager.reverseLayout = true

                    layoutManager = mLinearLayoutManager
                    adapter = MemoAdapter(context)
                    scrollToPosition(0)
                }.lparams(width = matchParent, height = wrapContent, weight = 1.0f){}

                linearLayout {
                    // Memo input
                    memoEditText = editText {
                        id = R.id.memo_edit_text
                        hintResource = R.string.memo_write_hint
                        maxLines = 5
                    }.lparams(width = wrapContent, height = wrapContent, weight = 1.0f){}

                    // Memo save button
                    button(R.string.memo_save) {
                        onClick {
                            val adapter = memoRecyclerView.adapter as MemoAdapter
                            val text = getMemo()
                            if (text != "") {
                                adapter.push(text)
                                memoRecyclerView.scrollToPosition(0)
                            }

                            clearMemo()
                        }
                    }.lparams(width = wrapContent, height = wrapContent){
                        gravity = Gravity.BOTTOM
                    }
                }.lparams(width = matchParent, height = wrapContent){}
            }.lparams(width = matchParent, height = matchParent){}

            // Floating buttons
            verticalLayout {
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
            }.lparams(width = matchParent, height = wrapContent){}
        }
    }

    private fun getMemo(): String {
        return memoEditText.text.toString().trim()
    }

    private fun clearMemo() {
        memoEditText.setText("")
    }
}
