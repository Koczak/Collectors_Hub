package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.collectors_hub.projection.UserProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.entity.User
import project.collectors_hub.repository.UserRepository
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun getAllUsers(): List<UserProjection> {
        return userRepository.getAllUsers()
    }

    override fun getUserByUsername(username: String): Optional<User> {
        return userRepository.findByUsername(username)
    }

    override fun createUser(dto: UserDto) {
        val user = User(
            username = dto.username,
            email = dto.email,
            password = passwordEncoder.encode(dto.password),
            roles = dto.roles,
            collections = emptyList(),
            categories = emptyList()
        )
        userRepository.save(user)
    }

    override fun deleteUser(id: Long): Boolean {
        val user = userRepository.findById(id).orElseThrow { throw EntityNotFoundException("User with id $id not found") }
        if (user.roles == User.ROLE_ADMIN) {
            throw IllegalArgumentException("Cannot delete admin user")
        }
        userRepository.delete(user)
        return true
    }

    override fun editUser(id: Long, dto: UserDto): Boolean {
        val user = userRepository.findById(id).orElseThrow { throw EntityNotFoundException("User with id $id not found") }
        user.username = dto.username
        user.email = dto.email
        user.password = passwordEncoder.encode(dto.password)
        userRepository.save(user)
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