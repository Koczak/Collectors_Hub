package project.collectors_hub.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.collectors_hub.dto.GetAllUsersProjection
import project.collectors_hub.dto.UserDto
import project.collectors_hub.dto.UserForm
import project.collectors_hub.entity.User
import project.collectors_hub.repository.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun getAllUsers(): List<GetAllUsersProjection> {
        return userRepository.getAllUsers()
    }

    override fun getUserByUsername(username: String): User? {
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