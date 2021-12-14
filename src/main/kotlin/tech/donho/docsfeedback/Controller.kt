package tech.donho.docsfeedback

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


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
@RequestMapping("doc/{docId}/ratings")
class RatingController @Autowired constructor(
    val ratingRepository: RatingRepository,
    val documentationRepository: DocumentationRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{id}")
    fun getCurrent(@PathVariable docId: Int, @PathVariable id: Int): RatingDto {
        return RatingDto.create(ratingRepository.findById(id).get())
    }

    @CrossOrigin
    @GetMapping
    fun getAll(@PathVariable docId: Int): List<RatingDto> {
        return ratingRepository.findByDocumentationId(docId).map { RatingDto.create(it) }
    }

    @CrossOrigin
    @GetMapping("/search")
    fun search(@PathVariable docId: Int, request: HttpServletRequest): List<RatingDto> {
        log.info(request.queryString);
        return ratingRepository.findByDocumentationId(docId).map { RatingDto.create(it) }
    }

    @CrossOrigin
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@PathVariable docId: Int, @RequestBody dto: RatingDto): String {
        val doc = documentationRepository.findById(docId).get()
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
