package project.collectors_hub.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

object SecurityUtils {

    fun getCurrentUsername(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication != null && authentication.isAuthenticated) {
            val principal = authentication.principal
            if (principal is UserDetails) {
                principal.username
            } else {
                principal.toString()
            }
        } else {
            null
        }
    }

}