package com.khtn.ratevid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khtn.ratevid.R


class RankingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_ranking, container, false)
        return view
    }



//    fun swap(A: ArrayList<ModelVideo>?, i: Int, j: Int) {
//        var temp = A?.get(i)
//        A?.set(i, A?.get(j))
//        if (temp != null) {
//            A?.set(j, temp)
//        }
//    }
//
//    fun max_heapify(A: ArrayList<ModelVideo>?,heapSize:Int, i: Int) {
//        var l = 2 * i;
//        var r = 2 * i+1;
//        var largest: Int;
//
//        if ((l <= heapSize - 1) && (A?.get(l)?.RateAVG!! < A?.get(i).RateAVG!!)) {
//            largest = l;
//        } else
//            largest = i
//
//        if ((r <= heapSize - 1) && (A?.get(r)?.RateAVG!! < A?.get(l)?.RateAVG!!)) {
//            largest = r
//        }
//
//        if (largest != i) {
//            swap(A, i, largest);
//            max_heapify(A,heapSize, largest);
//        }
//    }
//
//    fun heap_sort(A: ArrayList<ModelVideo>?) {
//        var heapSize = A?.size
//        if (heapSize != null) {
//            for (i in heapSize / 2 downTo 0) {
//                if (heapSize != null) {
//                    max_heapify(A,heapSize, i)
//                }
//            }
//        }
//        if (A != null) {
//            for (i in A.size - 1 downTo 1) {
//                swap(A, i, 0)
//                if (heapSize != null) {
//                    heapSize = heapSize - 1
//                }
//                if (heapSize != null) {
//                    max_heapify(A,heapSize, 0)
//                }
//
//            }
//        }
//    }

}