package com.template.core.presentation.ui

import com.template.core.domain.util.DataError

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_timeout)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
        DataError.Network.SERIALIZATION -> UiText.StringResource(R.string.error_serialization)
        DataError.Local.DISK_FULL -> UiText.StringResource(R.string.error_disk_full)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}
