package io.github.yunik1004.logmemo.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yunik1004.logmemo.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

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

data class Memo(var time: String, var text: String)

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private val memoList: ArrayList<Memo> = ArrayList()

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
