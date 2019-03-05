package com.kv.wanandroid.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kv.wanandroid.R

/**
 * ========================================================
 *
 * @author: Created by Vension on 2018/10/17 11:35.
 * @email: 2506856664@qq.com
 * @desc: character determines attitude, attitude determines destiny
 * ========================================================
 */
class TestFragment : Fragment() {

    companion object {
        fun getInstance(title: String): TestFragment {
            val fragment = TestFragment()
            fragment.mTitle = title
            return fragment
        }
    }

    private var tvTitle: TextView? = null
    private var mTitle: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitle = view.findViewById(R.id.tv_content)
        arguments?.let {
            mTitle = it.getString("search_key")
            tvTitle?.text = mTitle
        }
    }


}
