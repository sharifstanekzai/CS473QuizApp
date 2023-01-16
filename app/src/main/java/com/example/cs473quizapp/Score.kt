package com.example.cs473quizapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.cs473quizapp.databinding.FragmentQuizBinding
import com.example.cs473quizapp.databinding.FragmentScoreBinding

class Score : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = arguments?.getString(RESULT_KEY) ?: ""
        val description = arguments?.getString(DESCRIPTION_RESULT_KEY) ?: ""

        binding.result.text = result

        binding.backButton.setOnClickListener {
            val listener = activity as? ResultListener
            listener?.back()
        }

        binding.exitButton.setOnClickListener {
            activity?.finish()
        }

        binding.shareButton.setOnClickListener {
            composeEmail("Quiz results", description)
        }
    }

    private fun composeEmail(subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val packageManager = context?.packageManager ?: return
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    interface ResultListener {

        fun back()
    }

    companion object {

        private const val RESULT_KEY = "RESULT"
        private const val DESCRIPTION_RESULT_KEY = "DESCRIPTION_RESULT"

        fun newInstance(result: String, resultDescription: String): Score {
            return Score().apply {
                arguments = bundleOf(
                    RESULT_KEY to result,
                    DESCRIPTION_RESULT_KEY to resultDescription
                )
            }
        }
    }
}