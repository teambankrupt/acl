package com.example.acl.routing

class Route {
    object V1 {
        private const val API = "/api"
        private const val VERSION = "/v1"
        private const val ADMIN = "/admin"

        const val VERIFY_REGISTRATION = "$API$VERSION/register/verify"
        const val CHECK_USERNAME = "$API$VERSION/public/register/check-username"
        const val REGISTER = "$API$VERSION/register"
        const val CHANGE_PASSWORD = "$API$VERSION/change_password"
        const val RESET_PASSWORD = "$API$VERSION/reset_password"

        // Roles (Admin)
        const val WEB_ROLES_PAGE = "$ADMIN/roles"
        const val WEB_ROLE_DETAILS_PAGE = "$ADMIN/roles/{role_id}"
        const val WEB_ROLE_CREATE = "$ADMIN/roles"
        const val WEB_ROLE_UPDATE = "$ADMIN/roles/{role_id}"

        // Privileges (Admin)
        const val WEB_PRIVILEGES_PAGE = "$ADMIN/privileges"
        const val WEB_PRIVILEGE_CREATE = "$ADMIN/privileges"
        const val WEB_PRIVILEGE_DETAILS_PAGE = "$ADMIN/privileges/{privilege_id}"
        const val WEB_PRIVILEGE_UPDATE = "$ADMIN/privileges/{privilege_id}"

        // Users (Admin)
        const val WEB_USERS_SEARCH_PAGE = "$ADMIN/users"
        const val WEB_USERS_CREATE = "$ADMIN/users"
        const val WEB_USERS_DETAILS_PAGE = "$ADMIN/users/{user_id}"
        const val WEB_USERS_UPDATE = "$ADMIN/users/{user_id}"
        const val WEB_TOGGLE_ENABLED = "$ADMIN/users/{user_id}/toggle-access"

        const val WEB_REQUEST_DELETION = "/public/users/request-account-deletion"
        const val WEB_DELETE_ACCOUNT = "/users/delete-account"

        // Users
        const val FIND_AVATAR_URL = "$API$VERSION/public/users/{username}/avatar"
        const val FIND_ME = "$API$VERSION/users/me"
        const val DELETE_ME = "$API$VERSION/users/delete-me"
        const val UPDATE_AVATAR = "$API$VERSION/users/me/update-avatar"

        // User Admin Statistics
        const val ADMIN_USERS_GROWTH_STATS = "$API$VERSION$ADMIN/users/growth-statistics"

        // Profiles
        const val SEARCH_PROFILES = "$API$VERSION/profiles"
        const val CREATE_PROFILE = "$API$VERSION/profiles"
        const val FIND_PROFILE = "$API$VERSION/profiles/{id}"
        const val MY_PROFILE = "$API$VERSION/my-profile"
        const val UPDATE_PROFILE = "$API$VERSION/profiles/{id}"
        const val DELETE_PROFILE = "$API$VERSION/profiles/{id}"

        // Profiles (Admin)
        const val ADMIN_SEARCH_PROFILES = "$ADMIN/profiles"
        const val ADMIN_CREATE_PROFILE_PAGE = "$ADMIN/profiles/create"
        const val ADMIN_CREATE_PROFILE = "$ADMIN/profiles"
        const val ADMIN_FIND_PROFILE = "$ADMIN/profiles/{id}"
        const val ADMIN_UPDATE_PROFILE_PAGE = "$ADMIN/profiles/{id}/update"
        const val ADMIN_UPDATE_PROFILE = "$ADMIN/profiles/{id}"
        const val ADMIN_DELETE_PROFILE = "$ADMIN/profiles/{id}/delete"

        /*
        Request Credentials / lockouts
         */
        const val ADMIN_GET_LOCKOUTS = "$API$VERSION$ADMIN/request-credentials/lockouts"

        /*
        Get validation tokens
         */
        const val ADMIN_GET_VALIDATION_TOKENS = "$API$VERSION$ADMIN/validation-tokens"
    }
}
