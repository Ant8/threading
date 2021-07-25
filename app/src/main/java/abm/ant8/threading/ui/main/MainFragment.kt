package abm.ant8.threading.ui.main

import abm.ant8.threading.databinding.MainFragmentBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()

        private const val REQUIRED_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: MainFragmentBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean -> }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
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

        viewModel.requirePermissionsLiveData.observe(viewLifecycleOwner, { requirePermissions() })
        // TODO: Use the ViewModel
    }

    private fun requirePermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                REQUIRED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.checkThreadsPrerequisites()
            }
            else -> {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
        }
    }
}