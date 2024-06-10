package com.pukimen.babygrowth.ui.bottomNav.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.databinding.FragmentNotificationsBinding
import com.pukimen.babygrowth.ui.AboutActivity
import com.pukimen.babygrowth.ui.EditActivity
import com.pukimen.babygrowth.ui.TermsActivity
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.auth.LoginActivity
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext(), requireActivity().application)

        val viewModel: AuthViewModel by viewModels { factory }
        val FoodviewModel: FoodViewModel by viewModels { factory }

        binding.termsPolicy.setOnClickListener {
            val intent = Intent(context, TermsActivity::class.java)
            startActivity(intent)
        }
        binding.editBaby.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            startActivity(intent)
        }
        binding.about.setOnClickListener {
            val intent = Intent(context, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Alert!")
                setMessage("Are you sure want to log out?")
                setPositiveButton("Ok") { dialog, _ ->
                    viewModel.logout()
                    FoodviewModel.getAllNutrition().observe(viewLifecycleOwner) {
                        if (it != null) {
                            it.forEach {
                                FoodviewModel.delete(it)
                            }
                        }
                    }
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                    dialog.dismiss()

                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        }

        viewModel.getSession().observe(viewLifecycleOwner) {
            val birthday = calculateAgeInMonths(it.birthDay)
            binding.tvAge.text = "${birthday} Month"
            binding.tvName.text = it.name
        }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAgeInMonths(birthday: String?): Long {
        return if (!birthday.isNullOrEmpty()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val birthDate = LocalDate.parse(birthday, formatter)
                val currentDate = LocalDate.now()
                ChronoUnit.MONTHS.between(birthDate, currentDate)
            } catch (e: Exception) {
                // Log the error if needed
                0
            }
        } else {
            0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
