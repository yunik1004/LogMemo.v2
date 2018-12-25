package io.github.yunik1004.logmemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.yunik1004.logmemo.R
import io.github.yunik1004.logmemo.db.MemoRepository
import io.github.yunik1004.logmemo.model.Memo
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import kotlin.collections.ArrayList

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

class MemoAdapter(context: Context): RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private var memoRepository = MemoRepository(context)
    private var memoList: ArrayList<Memo> = memoRepository.findAll()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder(MemoUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memoList[position]
        holder.memoCreatedTime.text = memo.time
        holder.memoCreatedText.text = memo.text
        holder.itemView.onClick {
            memoRepository.delete(memo.id)
            val currentPosition = holder.adapterPosition
            memoList.removeAt(currentPosition)
            notifyItemRemoved(currentPosition)
        }
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    fun push(text: String) {
        val newMemo = memoRepository.insert(text)
        if (newMemo != null) {
            memoList.add(0, newMemo)
            notifyItemInserted(0)
        }
    }

    fun reset() {
        memoList.clear()
        memoRepository.deleteAll()
        notifyDataSetChanged()
    }

    inner class MemoViewHolder(memoView: View): RecyclerView.ViewHolder(memoView) {
        var memoCreatedTime: TextView = memoView.findViewById(R.id.memo_created_time)
        var memoCreatedText: TextView = memoView.findViewById(R.id.memo_created_text)
    }
}
