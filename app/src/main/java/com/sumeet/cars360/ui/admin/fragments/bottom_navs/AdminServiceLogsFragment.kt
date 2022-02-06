package com.sumeet.cars360.ui.admin.fragments.bottom_navs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sumeet.cars360.data.remote.model.service_logs.ServiceLogResponse
import com.sumeet.cars360.databinding.FragmentAdminServiceLogsBinding
import com.sumeet.cars360.ui.admin.AdminViewModel
import com.sumeet.cars360.ui.admin.OnServiceItemClickListener
import com.sumeet.cars360.ui.admin.ServiceLogRecyclerAdapter
import com.sumeet.cars360.util.Resource
import com.sumeet.cars360.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminServiceLogsFragment : Fragment(), OnServiceItemClickListener {

    private lateinit var binding: FragmentAdminServiceLogsBinding
    private val viewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminServiceLogsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerData()
    }

    private fun setUpRecyclerData(){
        viewModel.getAllServiceLogsByUserId("11")
        viewModel.allServiceLogs.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {}
                is Resource.Error -> {}
                is Resource.Success -> {
                    binding.serviceLogsRecyclerView.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = it.data?.serviceLogResponse?.let { data ->
                            ServiceLogRecyclerAdapter(
                                data,
                                this@AdminServiceLogsFragment
                            )
                        }
                    }
                }
            }
        })
    }

    override fun onServiceItemClicked(serviceLogResponse: ServiceLogResponse) {
        navigate(AdminServiceLogsFragmentDirections
            .actionAdminNavigationServiceLogsToServiceLogDetailsFragment(serviceLogResponse))
    }
}