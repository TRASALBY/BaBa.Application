package kids.baba.mobile.presentation.view.signup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentTextInputBabyInfoBinding
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel

class TextInputBabyInfoFragment : Fragment() {
    private var _binding: FragmentTextInputBabyInfoBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val inputBabiesInfoViewModel: InputBabiesInfoViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextInputBabyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        focusEditText()
        setTextWatcher()
        binding.apply {
            etTextInput.setOnEditorActionListener(getEditorActionListener(btnSend))

            btnSend.setOnClickListener {
                when(val uiState = inputBabiesInfoViewModel.uiState.value) {
                    is InputBabiesInfoUiState.InputBabyName -> {
                        val babyName = etTextInput.text.toString()
                        uiState.babyInfo.babyName = babyName
                        inputBabiesInfoViewModel.setBabyName(
                            ChatItem.UserChatWithBabyInfoItem(
                                etTextInput.text.toString(),
                                UserChatType.BABY_NAME,
                                uiState.babyInfo,
                                isModifying = false
                            )
                        )
                    }
                    is InputBabiesInfoUiState.ModifyName -> {
                        val babyName = etTextInput.text.toString()
                        uiState.babyInfo.babyName = babyName
                        inputBabiesInfoViewModel.modifyBabyInfo(
                            ChatItem.UserChatWithBabyInfoItem(
                                babyName,
                                UserChatType.BABY_NAME,
                                uiState.babyInfo,
                                isModifying = false
                            ),
                            uiState.position
                        )
                    }
                    is InputBabiesInfoUiState.InputRelation -> {
                        inputBabiesInfoViewModel.setRelation(etTextInput.text.toString())
                    }

                    is InputBabiesInfoUiState.InputInviteCode -> {
                        inputBabiesInfoViewModel.setInviteCode(etTextInput.text.toString())
                    }
                    else -> {

                    }
                }
                etTextInput.setText("")
                hideKeyboard()
            }
        }
    }

    private fun focusEditText() {
        binding.etTextInput.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etTextInput, 0)
    }
    private fun setTextWatcher(){
        binding.etTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSend.apply {
                    if(s.isNullOrEmpty()){
                        isEnabled = false
                        setTextColor(requireContext().getColor(R.color.text_3))
                    } else {
                        isEnabled = true
                        setTextColor(requireContext().getColor(R.color.baba_main))
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etTextInput.windowToken, 0)
        binding.etTextInput.clearFocus()
    }

    private fun getEditorActionListener(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            false
        }
    }

}
