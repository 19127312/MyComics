package com.khtn.ratevid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.R
import com.khtn.ratevid.model.ModelVideo

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RankingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RankingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RankingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun swap(A: ArrayList<ModelVideo>?, i: Int, j: Int) {
        var temp = A?.get(i)
        A?.set(i, A?.get(j))
        if (temp != null) {
            A?.set(j, temp)
        }
    }

    fun max_heapify(A: ArrayList<ModelVideo>?,heapSize:Int, i: Int) {
        var l = 2 * i;
        var r = 2 * i+1;
        var largest: Int;

        if ((l <= heapSize - 1) && (A?.get(l)?.RateAVG!! < A?.get(i).RateAVG!!)) {
            largest = l;
        } else
            largest = i

        if ((r <= heapSize - 1) && (A?.get(r)?.RateAVG!! < A?.get(l)?.RateAVG!!)) {
            largest = r
        }

        if (largest != i) {
            swap(A, i, largest);
            max_heapify(A,heapSize, largest);
        }
    }

    fun heap_sort(A: ArrayList<ModelVideo>?) {
        var heapSize = A?.size
        if (heapSize != null) {
            for (i in heapSize / 2 downTo 0) {
                if (heapSize != null) {
                    max_heapify(A,heapSize, i)
                }
            }
        }
        if (A != null) {
            for (i in A.size - 1 downTo 1) {
                swap(A, i, 0)
                if (heapSize != null) {
                    heapSize = heapSize - 1
                }
                if (heapSize != null) {
                    max_heapify(A,heapSize, 0)
                }

            }
        }
    }

}