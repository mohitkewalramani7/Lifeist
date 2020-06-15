package com.example.lifeist.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lifeist.category.CreateAndEditCategoryActivity
import com.example.lifeist.category.Category
import com.example.lifeist.category.CategoryDisplayActivity
import com.example.lifeist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
    private lateinit var database: DatabaseReference
    private lateinit var categoriesList: ListView

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
        initializeCategoryListview()
        initializeAddCategoryButton()
        initializeListviewClickListener()
    }

    private fun initializeCategoryListview(){
        categoriesList = activity!!.findViewById(R.id.dashboardListView)
        categoriesList.adapter = populateListView()
    }

    private fun populateListView() : DashboardListAdapter {
        val dash = DashboardListAdapter()
        database = FirebaseDatabase.getInstance().reference

        val postListener = object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                dash.dashboardListItems.clear()
                val user = FirebaseAuth.getInstance().currentUser
                val categoryList = p0.child(user!!.uid)
                for (category in categoryList.children){
                    val a: HashMap<String, Any> = category.value as HashMap<String, Any>
                    dash.dashboardListItems.add(Category(
                        category.key!!,
                        a.get("title")!! as String,
                        a.get("description")!! as String,
                        checkNullTaskList(a) as HashMap<String, String>
                    ))
                }
                dash.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Cancelled READ: ", Toast.LENGTH_SHORT).show()
                Log.d("READCAT", p0.toException().toString())
            }
        }
        database.addValueEventListener(postListener)
        return dash
    }

    private fun checkNullTaskList(category: HashMap<String, Any>): Any{
        if (category.get("tasklist") === null){
            return HashMap<String, String>()
        }
        return category.get("tasklist")!!
    }

    private fun initializeAddCategoryButton(){
        val addCategoryFab = activity!!.findViewById<FloatingActionButton>(R.id.addCategoryButton)
        addCategoryFab.setOnClickListener {
            val intent = Intent(context, CreateAndEditCategoryActivity::class.java)
            intent.putExtra(
                CreateAndEditCategoryActivity.INTENT_TYPE,
                CreateAndEditCategoryActivity.CREATE_CATEGORY
            )
            startActivity(intent)
        }
    }

    private fun initializeListviewClickListener(){
        categoriesList.onItemClickListener = AdapterView.OnItemClickListener {_, _, pos, _ ->
            val clickedCategory: Category = categoriesList.adapter.getItem(pos) as Category
            val intent = Intent(context, CategoryDisplayActivity::class.java)
            intent.putExtra(
                CategoryDisplayActivity.CATEGORY_KEY,
                clickedCategory.key
            )
            intent.putExtra(
                CategoryDisplayActivity.CATEGORY_TITLE,
                clickedCategory.title
            )
            intent.putExtra(
                CategoryDisplayActivity.CATEGORY_DESCRIPTION,
                clickedCategory.description
            )
            intent.putExtra(
                "tasklist",
                clickedCategory.taskList
            )
            startActivity(intent)
        }
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
        setCategoryDescription(currentCategory.description, localConvertView)
        return localConvertView
    }

    private fun setCategoryText(title: String, localConvertView: View) {
        val categoryText = localConvertView.findViewById<TextView>(R.id.dashboardListItem)
        categoryText.text = title
    }

    private fun setCategoryDescription(description: String, localConvertView: View){
        val categoryDescription = localConvertView.findViewById<TextView>(R.id.categoryDescription)
        categoryDescription.text = description;
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
