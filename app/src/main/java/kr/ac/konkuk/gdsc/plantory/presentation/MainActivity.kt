package kr.ac.konkuk.gdsc.plantory.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.ActivityMainBinding
import kr.ac.konkuk.gdsc.plantory.presentation.home.HomeFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingActivity
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToHome()
        handleDeviceToken()
        setPostRegisterUserStateObserver()
    }

    private fun navigateToHome() {
        navigateTo<HomeFragment>()
    }

    private fun handleDeviceToken() {
        getCurrentDeviceTokenFromFirebase()?.let { currentToken ->
            checkAndUpdateDeviceToken(currentToken)
        }
    }

    private fun getCurrentDeviceTokenFromFirebase(): String? {
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
                Timber.d("Current Token: $token")
            } else Timber.e("Failed to connect Firebase")
        }
        return token
    }

    private fun checkAndUpdateDeviceToken(currentToken: String) {
        lifecycleScope.launch {
            val storedToken = viewModel.getDeviceToken()
            if (storedToken != currentToken || storedToken.isEmpty()) {
                viewModel.postRegisterUser()
            }
        }
    }

    private fun setPostRegisterUserStateObserver() {
        viewModel.postRegisterUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Timber.d("Success : Register ")
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> {
                }
            }
        }.launchIn(lifecycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.fcv_main, T::class.simpleName)
        }
    }
}
