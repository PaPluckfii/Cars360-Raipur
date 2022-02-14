package com.sumeet.cars360.ui.admin.fragments.home_navs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sumeet.cars360.data.remote.model.user.UserResponse
import com.sumeet.cars360.databinding.FragmentAllCustomersBinding
import com.sumeet.cars360.ui.admin.AdminViewModel
import com.sumeet.cars360.ui.admin.AllCustomerRecyclerAdapter
import com.sumeet.cars360.ui.admin.OnAllCustomerItemClickListener
import com.sumeet.cars360.util.Constants
import com.sumeet.cars360.util.Resource
import com.sumeet.cars360.util.ViewVisibilityUtil
import com.sumeet.cars360.util.navigate

class AllCustomersFragment : Fragment(), OnAllCustomerItemClickListener {
    private lateinit var binding: FragmentAllCustomersBinding
    private val viewModel: AdminViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCustomersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerData()
    }

    private fun setUpRecyclerData() {

        viewModel.getAllCustomerByUserId("2")
        viewModel.allCustomers.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    ViewVisibilityUtil.visibilityExchanger(visible = binding.progressBar
                        ,gone = binding.allCustomerRecyclerview)
                    ViewVisibilityUtil.gone(binding.errorMessage)
                }
                is Resource.Error -> {
                    ViewVisibilityUtil.gone(binding.allCustomerRecyclerview)
                    ViewVisibilityUtil.gone(binding.progressBar)
                    ViewVisibilityUtil.visible(binding.errorMessage)

                    if (it.message.equals(Constants.NO_INTERNET_CONNECTION))
                        binding.tvError.text = Constants.NO_INTERNET_CONNECTION
                    else
                        binding.tvError.text = it.message
                }
                is Resource.Success -> {
                    ViewVisibilityUtil.visibilityExchanger(visible = binding.allCustomerRecyclerview
                        ,gone = binding.progressBar)
                    ViewVisibilityUtil.gone(binding.errorMessage)
                    binding.allCustomerRecyclerview.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = it.data?.userResponse?.let { data ->
                            AllCustomerRecyclerAdapter(
                                data,
                                this@AllCustomersFragment
                            )
                        }
                    }
                }
            }
        })
    }

    override fun onAllCustomerItemClicked(userResponse: UserResponse) {
       navigate(AllCustomersFragmentDirections.actionAllCustomersFragmentToAllCustomerDetailsFragment(userResponse))
    }
}