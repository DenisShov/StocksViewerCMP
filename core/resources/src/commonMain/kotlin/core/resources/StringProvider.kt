package core.resources

import org.jetbrains.compose.resources.StringResource

/**
 * Multiplatform string provider for accessing localized strings
 * outside of Compose context (e.g., in mappers, ViewModels).
 *
 * Uses Compose Multiplatform Resources under the hood, reading from
 * commonMain/composeResources/values/strings.xml on all platforms.
 */
class StringProvider {
    suspend fun getString(resource: StringResource) =
        org.jetbrains.compose.resources.getString(resource)
}
