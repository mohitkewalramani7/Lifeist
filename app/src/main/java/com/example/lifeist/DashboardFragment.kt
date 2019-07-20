package com.example.lifeist

import android.app.Activity
import android.content.Context
import android.database.DataSetObserver
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Layout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.*

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

    private fun populateListView() : DashboardListAdapterTwo {
        val cardList = arrayListOf<String>()
        cardList.add("A")
        cardList.add("B")
        val dash = DashboardListAdapterTwo()
        dash.dashboardListItems = cardList
        return dash
    }

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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment().apply {}
    }
}

class DashboardListAdapterTwo() : BaseAdapter() {

    var dashboardListItems = ArrayList<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var localConvertView = convertView
        if (localConvertView == null){
            localConvertView = LayoutInflater.from(parent!!.context).inflate(R.layout.dashboard_card, parent, false)
        }
        val listItem = localConvertView!!.findViewById<TextView>(R.id.dashboardListItem)
        listItem.text = getItem(position) as String
        return localConvertView
    }

    override fun getItem(position: Int): Any {
        return dashboardListItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dashboardListItems.size
    }

}
