package abm.ant8.threading.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import abm.ant8.threading.R
import abm.ant8.threading.databinding.MainFragmentBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: MainFragmentBinding

    val requestPermissionLauncher =
    registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean -> }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        MainFragmentBinding
            .inflate(inflater, container, false)
            .apply {
                dataBinding = this
                lifecycleOwner = viewLifecycleOwner
            }
            .root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            .apply {
                dataBinding.viewmodel = this
            }
        // TODO: Use the ViewModel
    }

}