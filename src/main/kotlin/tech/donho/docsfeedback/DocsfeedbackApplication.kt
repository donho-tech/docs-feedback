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
interface UserRepository : CrudRepository<User, Long>

@Entity
class Documentation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val name: String,
)

@Repository
interface DocumentationRepository : CrudRepository<Documentation, Long>

@Entity
class Rating(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val helpful: Boolean,
    val comment: String,
    val referenceId: String,
    val link: String?,
    @ManyToOne @JoinColumn(name = "docs_id") val documentation: Documentation,
)

@Repository
interface RatingRepository : CrudRepository<Rating, Long>

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

@RestController
@RequestMapping("ratings")
class RatingController @Autowired constructor(
    val ratingRepository: RatingRepository,
    val documentationRepository: DocumentationRepository,
) {

    @GetMapping("/{id}")
    fun getCurrent(@PathVariable id: Long): Rating {
        return ratingRepository.findById(id).get()
    }

    @GetMapping("create")
    fun create(): String {
        val doc = documentationRepository.findById(1).get()
        val rating = Rating(null, true, "Das war sehr hilfreich!", "ErsterArtikel", "www.docs.de/ersterartikel", doc)
        ratingRepository.save(rating)
        return "created"
    }
}
