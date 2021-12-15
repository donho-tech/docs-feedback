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
@RequestMapping("doc")
class RatingController @Autowired constructor(
        val ratingRepository: RatingRepository,
        val documentationRepository: DocumentationRepository,
        val documentRepository: DocumentRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

//    @GetMapping()
//    fun getAllDocs(): RatingDto {
//        return documentationRepository.findAll();
//    }

    @GetMapping("/{docId}/ratings/{id}")
    fun getCurrent(@PathVariable docId: Int, @PathVariable id: Int): RatingOutputDto {
        return RatingOutputDto.create(ratingRepository.findById(id).get())
    }

    @CrossOrigin
    @GetMapping("/{docId}/ratings")
    fun getAll(@PathVariable docId: Int): List<RatingOutputDto> {
        return ratingRepository.findAll().map { RatingOutputDto.create(it) }
    }

    @CrossOrigin
    @PostMapping("/{docId}/ratings", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@PathVariable docId: Int, @RequestBody dto: RatingInputDto): String {
        val documentation = documentationRepository.findById(docId).get()
        val document = documentRepository.findByReferenceId(dto.referenceId)
        val rating = Rating(
                null,
                dto.helpful,
                dto.comment,
                document,
        )
        ratingRepository.save(rating)
        return "created"
    }
}
