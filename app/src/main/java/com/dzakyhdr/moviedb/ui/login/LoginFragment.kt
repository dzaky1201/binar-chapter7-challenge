package com.dzakyhdr.moviedb.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dzakyhdr.moviedb.R
import com.dzakyhdr.moviedb.databinding.FragmentLoginBinding
import com.dzakyhdr.moviedb.di.Injector
import com.dzakyhdr.moviedb.resource.Status
import com.dzakyhdr.moviedb.ui.viewmodelfactory.LoginViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class LoginFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels{
        loginViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as Injector).createLoginSubComponent().inject(this)

        viewModel.getStatus().observe(viewLifecycleOwner){ status ->
            if (status) {
                binding.loading.visibility = View.GONE
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                binding.loading.visibility = View.GONE
            }
        }

        binding.btnLogin.setOnClickListener {

            viewModel.login(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )


            viewModel.loginStatus.observe(viewLifecycleOwner) {user ->
                when (user.status) {
                    Status.SUCCESS -> {
                        if (user.data != null) {
                            viewModel.saveUserDataStore(true, user.data.id)
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Snackbar.make(
                                binding.root,
                                "User Tidak Ditemukan",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), user.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}