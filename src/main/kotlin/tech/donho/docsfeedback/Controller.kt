package tech.donho.docsfeedback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("user")
class UserController @Autowired constructor(
    val repository: UserRepository,
) {

    @GetMapping("/{id}")
    fun getCurrent(@PathVariable id: Int): User {
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
    fun getCurrent(@PathVariable id: Int): Rating {
        return ratingRepository.findById(id).get()
    }

    @PostMapping
    fun create(@RequestBody dto: RatingDto): String {
        val doc = documentationRepository.findById(dto.documentation).get()
        val rating = Rating(
            null,
            dto.helpful,
            dto.comment,
            dto.referenceId,
            dto.link,
            doc
        )
        ratingRepository.save(rating)
        return "created"
    }
}
