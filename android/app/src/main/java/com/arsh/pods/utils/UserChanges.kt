package com.arsh.pods.utils

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlin.time.Duration

/**
 * For a flow of what a control shows: leaves out the value it opens with, so that only changes made
 * afterwards are acted on, each once it has stayed put for [quiet].
 */
@OptIn(FlowPreview::class)
fun <T> Flow<T>.userChanges(quiet: Duration): Flow<T> = drop(1).debounce(quiet)
