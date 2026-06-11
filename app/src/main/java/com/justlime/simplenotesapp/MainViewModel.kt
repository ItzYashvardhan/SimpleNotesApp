package com.justlime.simplenotesapp

import androidx.lifecycle.ViewModel
import com.justlime.simplenotesapp.utils.currentTimeZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val date = flow {
        while (true){
            emit(Clock.System.now().toLocalDateTime(currentTimeZone))
            delay(1.seconds)
        }
    }

}