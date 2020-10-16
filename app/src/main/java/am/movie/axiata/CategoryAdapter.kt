package am.movie.axiata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var categoryList = arrayOf("business", "health", "science", "technology", "entertainment", "sports")


    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    inner class ViewHolder (view: View) :RecyclerView.ViewHolder(view) {
        private val tvCategory = view.tvTitle
        private val llCategory = view.llCategory

        fun bind (category: String) {
            tvCategory.text = category

            if (mOnItemClickListener != null) {
                llCategory.setOnClickListener {
                    mOnItemClickListener?.onItemClick(category)
                }
            }
        }
    }

}

interface OnItemClickListener{
    fun onItemClick(user: String)
}