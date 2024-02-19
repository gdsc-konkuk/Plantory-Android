package kr.ac.konkuk.gdsc.plantory.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kr.ac.konkuk.gdsc.plantory.util.activity.applyScreenEnterAnimation
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleDeviceToken()
        setPostRegisterUserStateObserver()
    }

    private fun handleDeviceToken() {
        lifecycleScope.launch {
            val currentToken = getCurrentDeviceTokenFromFirebase()
            currentToken?.let {
                checkAndUpdateDeviceToken(it)
            }
        }
    }

    private suspend fun getCurrentDeviceTokenFromFirebase(): String? =
        withContext(Dispatchers.IO) {
            runCatching { FirebaseMessaging.getInstance().token.await() }
                .onSuccess { token ->
                    Timber.d("Current Token: $token")
                }
                .onFailure { e ->
                    Timber.e("Failed to connect Firebase: ${e.message}")
                }
                .getOrNull()
        }

    private fun checkAndUpdateDeviceToken(currentToken: String) {
        lifecycleScope.launch {
            val storedToken = viewModel.getDeviceToken()
            Timber.d("Stored Token: $storedToken")
            if ((storedToken != currentToken) || storedToken.isEmpty()) {
                viewModel.postRegisterUser(currentToken)
            } else {
                navigateToMain()
            }
        }
    }

    private fun setPostRegisterUserStateObserver() {
        viewModel.postRegisterUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> Unit

                is UiState.Success -> {
                    Timber.d("Success : Register ")
                    navigateToMain()
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun navigateToMain() {
        navigateTo<MainActivity>()
        applyScreenEnterAnimation()
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this, T::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}
