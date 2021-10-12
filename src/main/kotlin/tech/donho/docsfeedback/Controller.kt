package tech.donho.docsfeedback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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
