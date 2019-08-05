package com.example.lifeist.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lifeist.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PreferencesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PreferencesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val USER_NAME = "Mohit"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container, false)
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
        setPreferencesHeaderText()
    }

    private fun setPreferencesHeaderText(){
        val header = activity!!.findViewById<TextView>(R.id.preferencesHeader)
        header.text = String.format("%s %s!", activity!!.resources.getString(R.string.hello), USER_NAME)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PreferencesFragment().apply {}
    }
}
