package com.cluster.bookstore.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cluster.bookstore.R
import com.cluster.bookstore.adapter.DashboardRecyclerAdapter
import com.cluster.bookstore.model.Book
import com.cluster.bookstore.util.ConnectionManager
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var btnCheckInternet: Button

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

//    var bookList = arrayListOf(
//        "Life of Pie",
//        "Five Fall into Adventures",
//        "Panchtantra",
//        "Harry Potter",
//        "Flamingo",
//        "2 States",
//        "The Pursuit of Happyness",
//        "Sherlock Holmes",
//        "Ikigai",
//        "Playing it My Way"
//    )

    val bookInfoList = arrayListOf<Book>()
//        Book("Life of Pie", "Aryan Gupta", "Rs 289", R.drawable.image_1),
//        Book("Five Fall into Adventures", "Arpan Gupta", "Rs 299", R.drawable.image_2),
//        Book("Panchtantra", "Soumya Gupta", "Rs 259", R.drawable.image_3),
//        Book("Harry Potter", "Suyash Gupta", "Rs 309", R.drawable.image_4),
//        Book("Flamingo", "Anjali Gupta", "Rs 209", R.drawable.image_5),
//        Book("2 States", "Khushi Gupta", "Rs 219", R.drawable.image_6),
//        Book("The Pursuit of Happyness", "Sagar Gupta", "Rs 189", R.drawable.image_7),
//        Book("Sherlock Holmes", "Sujal Gupta", "Rs 489", R.drawable.image_8),
//        Book("Ikigai", "Saket Gupta", "Rs 189", R.drawable.image_9),
//        Book("Playing it My Way", "Preeti Gupta", "Rs 249", R.drawable.image_10),

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        // container is the ViewGroup where the Dashboard Fragment is placed
        // attachToRoot is a boolean value asking to keep fragment permanently or not

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        btnCheckInternet.setOnClickListener {
            if(ConnectionManager().checkConnectivity(activity as Context)){
                // Internet is available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok"){text, listener ->
                    // Do nothing
                }
                dialog.setNegativeButton("Cancel"){text, listener ->
                    // Do nothing
                }
                dialog.create()
                dialog.show()
            }
            else{
                // Internet is not available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Ok"){text, listener ->
                    // Do nothing
                }
                dialog.setNegativeButton("Cancel"){text, listener ->
                    // Do nothing
                }
                dialog.create()
                dialog.show()
            }
        }
        layoutManager = LinearLayoutManager(activity)

//        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
//
//        recyclerDashboard.adapter = recyclerAdapter
//
//        recyclerDashboard.layoutManager = layoutManager
//
//        recyclerDashboard.addItemDecoration(
//            DividerItemDecoration(
//                recyclerDashboard.context,
//                (layoutManager as LinearLayoutManager).orientation
//            )
//        )

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object:JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{
                //Here we will handle the response
                try{
                    progressLayout.visibility = View.GONE
                    // println("Response is $it")
                    val success = it.getBoolean("success")

                    if(success){
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)

                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager

//                            recyclerDashboard.addItemDecoration(
//                                DividerItemDecoration(
//                                    recyclerDashboard.context,
//                                    (layoutManager as LinearLayoutManager).orientation
//                                )
//                            )
                        }
                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: JSONException){
                    Toast.makeText(activity as Context, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener{
                //Here we wiill handle the errors
                //println("Response is $it")
                Toast.makeText(activity as Context, "Volley error Occurred!!!", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String>{
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}