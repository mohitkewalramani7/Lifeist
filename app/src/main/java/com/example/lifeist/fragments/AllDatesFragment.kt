package com.example.lifeist.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.lifeist.HomeActivity
import com.example.lifeist.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AllDatesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AllDatesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AllDatesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_dates, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a: ListView = activity!!.findViewById(R.id.datesListView)
        a.adapter = populateListView()
    }

    private fun populateListView() : AllDatesListAdapter {
        val dash = AllDatesListAdapter()
        dash.tasksListDates = HomeActivity.dateList
        return dash
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllDatesFragment().apply {}
    }

}

internal class AllDatesListAdapter : BaseAdapter() {

    var tasksListDates = ArrayList<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var localConvertView = convertView
        if (localConvertView == null){
            localConvertView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.all_dates_list_item, parent, false)
        }
        val currentDate: String = getItem(position)
        setTaskDate(currentDate, localConvertView!!)
        return localConvertView
    }

    private fun setTaskDate(currentDate: String, localConvertView: View) {
        val a = localConvertView.findViewById<TextView>(R.id.all_dates_list_item_date)
        a.text = currentDate
    }

    override fun getItem(position: Int): String {
        return tasksListDates[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return tasksListDates.size
    }

}
