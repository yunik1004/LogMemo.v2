package io.github.yunik1004.logmemo.adapter

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
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

class MemoAdapter(private var context: Context): RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private var memoRepository = MemoRepository(context)
    private var memoList: ArrayList<Memo> = memoRepository.findAll()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder(MemoUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memoList[position]
        holder.memoCreatedTime.text = memo.time
        holder.memoCreatedText.text = memo.text

        setMemoComponentSettingPopupMenu(context, holder, memo.id)
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

    private fun setMemoComponentSettingPopupMenu(context: Context, holder: MemoViewHolder, memoId: Int) {
        val memoComponentSettingPopupMenu = PopupMenu(context, holder.itemView)
        memoComponentSettingPopupMenu.menu.add(1, R.id.memo_component_setting_modify, 1, R.string.memo_component_setting_modify)
        memoComponentSettingPopupMenu.menu.add(1, R.id.memo_component_setting_remove, 2, R.string.memo_component_setting_remove)
        memoComponentSettingPopupMenu.setOnMenuItemClickListener{item: MenuItem? ->
            val currentPosition = holder.adapterPosition
            when(item!!.itemId) {
                R.id.memo_component_setting_modify -> {
                    showMemoComponentModifyDialog(context, memoId, currentPosition)
                }
                R.id.memo_component_setting_remove -> {
                    memoRepository.delete(memoId)
                    memoList.removeAt(currentPosition)
                    notifyItemRemoved(currentPosition)
                }
            }
            true
        }

        holder.itemView.onClick {
            memoComponentSettingPopupMenu.show()
        }
    }

    private fun showMemoComponentModifyDialog(context: Context, memoId: Int, currentPosition: Int) {
        val builder = AlertDialog.Builder(context)
        val editText = EditText(context)
        editText.setText(memoList[currentPosition].text)
        editText.setSelection(editText.text.length)
        builder.setTitle(R.string.memo_component_setting_modify)
        builder.setView(editText)
        builder.setPositiveButton(R.string.dialog_ok){_, _ ->
            val newMemo = editText.text.toString()
            memoRepository.update(memoId, newMemo)
            memoList[currentPosition].text = newMemo
            notifyItemChanged(currentPosition)
        }
        builder.setNegativeButton(R.string.dialog_cancel){_, _ -> }

        builder.create()
        builder.show()
    }

    inner class MemoViewHolder(memoView: View): RecyclerView.ViewHolder(memoView) {
        var memoCreatedTime: TextView = memoView.findViewById(R.id.memo_created_time)
        var memoCreatedText: TextView = memoView.findViewById(R.id.memo_created_text)
    }
}
