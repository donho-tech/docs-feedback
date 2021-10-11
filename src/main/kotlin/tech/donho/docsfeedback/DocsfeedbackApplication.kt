package tech.donho.docsfeedback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import javax.persistence.*

@SpringBootApplication
class DocsfeedbackApplication

fun main(args: Array<String>) {
	runApplication<DocsfeedbackApplication>(*args)
}

@Entity
@Table(name = "users")
class User(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
	val name: String,
)

@Repository
interface UserRepository : CrudRepository<User, Long> {
}

@RestController
@RequestMapping("user")
class UserController @Autowired constructor(
	val repository: UserRepository,
) {

	@GetMapping("/{id}")
	fun getCurrent(@PathVariable id: Long): User {
		return repository.findById(id).get()
	}

	@GetMapping("create")
	fun create(): String {
		val user = User(null, "peter")
		repository.save(user)
		return "created"
	}
}
