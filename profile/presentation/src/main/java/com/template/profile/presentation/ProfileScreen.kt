package com.template.profile.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.template.core.presentation.designsystem.TemplateTheme
import com.template.core.presentation.ui.ObserveAsEvents
import com.template.profile.domain.AppLanguage
import com.template.profile.domain.ThemeMode
import com.template.profile.domain.UserProfile
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // Composable-local state: naturally resets on Activity recreation (config change),
    // unlike ViewModel state which survives it. This prevents the infinite loading bug.
    var isChangingLanguage by remember { mutableStateOf(false) }
    var pendingLocaleTag by remember { mutableStateOf<String?>(null) }

    // Apply the locale change after the scrim has rendered (one frame delay).
    LaunchedEffect(pendingLocaleTag) {
        val tag = pendingLocaleTag ?: return@LaunchedEffect
        // Small delay so Compose can render the scrim overlay before Activity recreation.
        kotlinx.coroutines.delay(200)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
            localeManager.applicationLocales = android.os.LocaleList.forLanguageTags(tag)
        } else {
            (context as? android.app.Activity)?.recreate()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }
            ProfileEvent.LogoutSuccess -> onLogout()
            is ProfileEvent.LanguageChanged -> {
                isChangingLanguage = true
                pendingLocaleTag = event.localeTag
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ProfileScreen(
            state = viewModel.state,
            onAction = viewModel::onAction
        )

        // Animated semi-transparent scrim overlay during language change.
        // Uses fadeIn/fadeOut for a polished transition instead of an abrupt full-screen block.
        AnimatedVisibility(
            visible = isChangingLanguage,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.profile_changing_language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Avatar
        if (state.userProfile != null) {
            AsyncImage(
                model = state.userProfile.image,
                contentDescription = state.userProfile.fullName,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.userProfile.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${state.userProfile.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = state.userProfile.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else if (state.isLoading) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Settings section header
        Text(
            text = stringResource(R.string.profile_settings),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Theme setting
        SettingsItem(
            icon = Icons.Default.DarkMode,
            title = stringResource(R.string.profile_theme),
            subtitle = when (state.themeMode) {
                ThemeMode.SYSTEM -> stringResource(R.string.profile_theme_system)
                ThemeMode.LIGHT -> stringResource(R.string.profile_theme_light)
                ThemeMode.DARK -> stringResource(R.string.profile_theme_dark)
            },
            onClick = { onAction(ProfileAction.OnShowThemeDialog) }
        )

        // Language setting
        SettingsItem(
            icon = Icons.Default.Language,
            title = stringResource(R.string.profile_language),
            subtitle = when (state.language) {
                AppLanguage.ENGLISH -> "English"
                AppLanguage.INDONESIAN -> "Bahasa Indonesia"
                AppLanguage.JAPANESE -> "日本語"
            },
            onClick = { onAction(ProfileAction.OnShowLanguageDialog) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        OutlinedButton(
            onClick = { onAction(ProfileAction.OnLogoutClick) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.profile_logout))
        }

        // Theme dialog
        if (state.showThemeDialog) {
            ThemeSelectionDialog(
                currentMode = state.themeMode,
                onSelect = { onAction(ProfileAction.OnThemeModeChange(it)) },
                onDismiss = { onAction(ProfileAction.OnDismissThemeDialog) }
            )
        }

        // Language dialog
        if (state.showLanguageDialog) {
            LanguageSelectionDialog(
                currentLanguage = state.language,
                onSelect = { onAction(ProfileAction.OnLanguageChange(it)) },
                onDismiss = { onAction(ProfileAction.OnDismissLanguageDialog) }
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentMode: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.profile_theme)) },
        text = {
            Column {
                ThemeMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = mode == currentMode,
                            onClick = { onSelect(mode) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (mode) {
                                ThemeMode.SYSTEM -> stringResource(R.string.profile_theme_system)
                                ThemeMode.LIGHT -> stringResource(R.string.profile_theme_light)
                                ThemeMode.DARK -> stringResource(R.string.profile_theme_dark)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.template.core.presentation.ui.R.string.cancel))
            }
        }
    )
}

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.profile_language)) },
        text = {
            Column {
                AppLanguage.entries.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = lang == currentLanguage,
                            onClick = { onSelect(lang) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (lang) {
                                AppLanguage.ENGLISH -> "English"
                                AppLanguage.INDONESIAN -> "Bahasa Indonesia"
                                AppLanguage.JAPANESE -> "日本語"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(com.template.core.presentation.ui.R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    TemplateTheme {
        ProfileScreen(
            state = ProfileState(
                userProfile = UserProfile(
                    id = 1,
                    username = "emilys",
                    email = "emily.johnson@example.com",
                    firstName = "Emily",
                    lastName = "Johnson",
                    gender = "female",
                    image = "",
                    phone = "+1234567890"
                ),
                themeMode = ThemeMode.SYSTEM,
                language = AppLanguage.ENGLISH
            ),
            onAction = {}
        )
    }
}
