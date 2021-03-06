package com.dzakyhdr.moviedb.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dzakyhdr.moviedb.R
import com.dzakyhdr.moviedb.data.local.auth.User
import com.dzakyhdr.moviedb.databinding.FragmentProfileBinding
import com.dzakyhdr.moviedb.di.Injector
import com.dzakyhdr.moviedb.resource.Status
import com.dzakyhdr.moviedb.ui.viewmodelfactory.ProfileViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory
    private val viewModel: ProfileViewModel by viewModels{
        profileViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as Injector).createProfilenSubComponent().inject(this)

        val userData = User()

        viewModel.getIdUser().observe(viewLifecycleOwner) {
            viewModel.userData(it)
        }

        viewModel.userData.observe(viewLifecycleOwner) { user ->
            when (user.status) {
                Status.SUCCESS -> {
                    if (user.data != null) {

                        userData.id = user.data.id

                        userData.password = user.data.password

                        userData.fullname = user.data.fullname
                        binding.edtFullname.setText(user.data.fullname)

                        userData.username = user.data.username
                        binding.edtUsername.setText(user.data.username)

                        userData.ttl = user.data.ttl
                        binding.edtLahir.setText(user.data.ttl)

                        userData.address = user.data.address
                        binding.edtAddress.setText(user.data.address)

                        userData.email = user.data.email
                        binding.edtEmail.setText(user.data.email)

                        userData.image = user.data.image
                        val uriImage = Uri.parse(user.data.image)
                        binding.imgProfile.setImageURI(uriImage)
                        Glide.with(binding.root).load(user.data.image)
                            .circleCrop()
                            .into(binding.imgProfile)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "User Tidak Ditemukan",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), user.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }



        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)

        }

        binding.btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(view.context)
            dialog.setTitle("Logout")
            dialog.setMessage("Apakah Anda Yakin Ingin Logout ?")
            dialog.setPositiveButton("Yakin") { _, _ ->
                Snackbar.make(binding.root, "User Berhasil Logout", Snackbar.LENGTH_LONG)
                    .show()
                viewModel.clearDataUser()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            dialog.setNegativeButton("Batal") { listener, _ ->
                listener.dismiss()
            }

            dialog.show()
        }
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(
                    userData
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}