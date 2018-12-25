package io.github.yunik1004.logmemo

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainUI: MainActivityUi

    val data = ArrayList<String>()

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
                memoRecyclerView = recyclerView {
                    val mLinearLayoutManager = LinearLayoutManager(context)
                    mLinearLayoutManager.reverseLayout = true

                    layoutManager = mLinearLayoutManager
                    adapter = MemoAdapter()
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
                            adapter.push(getMemo())
                            memoRecyclerView.scrollToPosition(0)
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
        return memoEditText.text.toString()
    }

    private fun clearMemo() {
        memoEditText.setText("")
    }
}

data class Memo(var time: String, var text: String)

class MemoUI: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            verticalPadding = dip(8)

            isClickable = true
            isFocusable = true
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            backgroundResource = outValue.resourceId

            onClick {
                toast("Memo click").show()
            }

            textView {
                id = R.id.memo_created_time
            }.lparams(width = wrapContent, height = wrapContent){
                rightMargin = dip(8)
            }

            textView {
                id = R.id.memo_created_text
            }.lparams(width = wrapContent, height = wrapContent, weight = 1.0f){}
        }
    }
}

class MemoAdapter(private val memoList: ArrayList<Memo> = ArrayList()): RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder(MemoUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memoList[position]
        holder.memoCreatedTime.text = memo.time
        holder.memoCreatedText.text = memo.text
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    fun push(text: String) {
        val textTrim = text.trim()
        if (textTrim == "") {
            return
        }

        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)

        memoList.add(0, Memo(currentTime, textTrim))
        notifyItemInserted(0)
    }

    inner class MemoViewHolder(memoView: View): RecyclerView.ViewHolder(memoView) {
        var memoCreatedTime: TextView = memoView.findViewById(R.id.memo_created_time)
        var memoCreatedText: TextView = memoView.findViewById(R.id.memo_created_text)
    }
}
