package com.example.newmyweaterproject.ui.main.view

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.FragmentContactsBinding


class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val permissionResult = registerForActivityResult(
        ActivityResultContracts
            .RequestPermission()
    ) {
        if (it) {
            getContacts()
        } else {
            Toast.makeText(
                requireActivity(), requireActivity()
                    .resources.getText(R.string.warning_permission), Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(
            inflater, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getContacts() {

        with(requireActivity()) {
            contentResolver
            val cursor: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let {
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name =
                            cursor.getString(
                                cursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            )
                        addView(requireContext(), name)
                    }
                }
            }
            cursor?.close()
        }
    }

    private fun addView(context: Context, textToShow: String) {
        binding.containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}