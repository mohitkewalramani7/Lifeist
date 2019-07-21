package com.example.lifeist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DashboardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DashboardFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lv: ListView = activity!!.findViewById(R.id.dashboardListView)
        lv.adapter = populateListView()
    }

    private fun populateListView() : DashboardListAdapter {
        val dash = DashboardListAdapter()
        dash.dashboardListItems = HomeActivity.categoryList
        return dash
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment().apply {}
    }
}

internal class DashboardListAdapter : BaseAdapter() {

    var dashboardListItems = ArrayList<Category>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var localConvertView = convertView
        if (localConvertView == null){
            localConvertView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.dashboard_list_item, parent, false)
        }
        val currentCategory: Category = getItem(position)
        setCategoryText(currentCategory.title, localConvertView!!)
        setCategoryCount(currentCategory.size, localConvertView)
        return localConvertView
    }

    private fun setCategoryText(title: String, localConvertView: View) {
        val categoryText = localConvertView.findViewById<TextView>(R.id.dashboardListItem)
        categoryText.text = title
    }

    private fun setCategoryCount(count: Int, localConvertView: View){
        val categoryCount = localConvertView.findViewById<TextView>(R.id.listItemCount)
        if (count > 1){
            categoryCount.text = String.format("%d items", count)
        }
        else{
            categoryCount.text = String.format("%d item", count)
        }
    }

    override fun getItem(position: Int): Category {
        return dashboardListItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dashboardListItems.size
    }

}
