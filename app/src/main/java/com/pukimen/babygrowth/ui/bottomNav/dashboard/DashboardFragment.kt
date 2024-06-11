package com.pukimen.babygrowth.ui.bottomNav.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.FragmentDashboardBinding
import com.pukimen.babygrowth.ui.HomeActivity
import com.pukimen.babygrowth.ui.ScanActivity
import com.pukimen.babygrowth.ui.ScanViewModel
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.utils.getImageUri
import java.io.File
import com.yalantis.ucrop.UCrop



class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var launcherIntentCamera: ActivityResultLauncher<Uri>
    private var currentImageUri: Uri? = null

    private lateinit var scanViewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelFactory = ViewModelFactory.getInstance(requireContext(), requireActivity().application)
        scanViewModel = ViewModelProvider(this, viewModelFactory).get(ScanViewModel::class.java)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isAccepted: Boolean ->
            if (isAccepted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }

        launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                currentImageUri?.let { uri ->
                    UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, "cropped_image")))
                        .start(requireContext(), this@DashboardFragment)
                }
            } else {
                navigateToHome()
            }
        }

        if (allPermissionsAllow()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun allPermissionsAllow() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val currentImageUri = getImageUri(requireContext())
        this.currentImageUri = currentImageUri
        launcherIntentCamera.launch(currentImageUri)
    }
    private fun setupUCrop(): UCrop.Options {
        return UCrop.Options().apply {
            setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            setToolbarWidgetColor(ContextCompat.getColor(requireContext(), R.color.white))
            setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
        }
    }
    private fun navigateToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(data!!)
            if (croppedUri != null) {
                val intent = Intent(requireContext(), ScanActivity::class.java).apply {
                    putExtra("IMAGE_URI", croppedUri.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Failed to crop image", Toast.LENGTH_SHORT).show()
            }
        }else if (resultCode == Activity.RESULT_CANCELED) {
            navigateToHome()
        }
    }
}