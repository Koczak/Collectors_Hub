package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.entity.User
import project.collectors_hub.exception.PermissionDeniedException
import project.collectors_hub.repository.UserRepository
import project.collectors_hub.security.SecurityUtils
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun getAllUsers(): List<UserProjection> {
        return userRepository.getAllUsers()
    }

    override fun getCurrentUser(): Optional<User> {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        return userRepository.findByUsername(username)
    }

    override fun getUserByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

    override fun createUser(dto: UserDto) {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val currentUser = getUserByUsername(username).orElseThrow { EntityNotFoundException("User $username not found") }
        if (currentUser.isAdmin()) {
            val user = User(
                username = dto.username,
                email = dto.email,
                password = passwordEncoder.encode(dto.password),
                roles = dto.roles,
                collections = emptyList(),
                categories = emptyList()
            )
            userRepository.save(user)
        } else {
            throw PermissionDeniedException("Only admin can create new users")
        }
    }

    override fun deleteUser(id: Long): Boolean {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val currentUser = getUserByUsername(username).orElseThrow { EntityNotFoundException("User $username not found") }
        if (currentUser.isAdmin()) {
            val user = userRepository.findById(id).orElseThrow { throw EntityNotFoundException("User with id $id not found") }
            if (user.roles == User.ROLE_ADMIN) {
                throw PermissionDeniedException("Cannot delete admin user account")
            }
            userRepository.delete(user)
        } else {
            throw PermissionDeniedException("Only admin can delete user account")
        }
        return true
    }

    override fun editUser(id: Long, dto: UserDto): Boolean {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val currentUser = getUserByUsername(username).orElseThrow { EntityNotFoundException("User $username not found") }
        if (currentUser.isAdmin() || currentUser.id == id) {
            val user = userRepository.findById(id).orElseThrow { throw EntityNotFoundException("User with id $id not found") }
            user.username = dto.username
            user.email = dto.email
            user.password = passwordEncoder.encode(dto.password)
            userRepository.save(user)
        } else {
            throw PermissionDeniedException("Only admin or owner can edit user account")
        }

        return true
    }

//    override fun createUser(userForm: UserForm) {
//        val user = User(
//            username = userForm.username,
//            email = userForm.email,
//            password = passwordEncoder.encode(userForm.password),
//            roles = User.ROLE_USER,
//            collections = emptyList(),
//            categories = emptyList()
//        )
//        userRepository.save(user)
//    }
}