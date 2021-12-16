package tech.donho.docsfeedback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

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
@RequestMapping("documentation")
class DocumentationController @Autowired constructor(
        val ratingRepository: RatingRepository,
        val documentationRepository: DocumentationRepository,
        val documentRepository: DocumentRepository,
) {
    @PostMapping
    fun createDocumentation(@RequestBody dto: DocumentationDto, request: HttpServletRequest): ResponseEntity<Any> {
        val documentation = Documentation(null, dto.name, emptyList())
        val saved = documentationRepository.save(documentation)

        return ResponseEntity.created(URI(request.requestURL.toString() + "/" + saved.id)).build()
    }

    @GetMapping
    fun getAllDocumentations(): ResponseEntity<List<DocumentationDto>>? {
        return ResponseEntity.ok(documentationRepository.findAll().map { DocumentationDto.create(it) })
    }

    @GetMapping("/{docId}")
    fun getDocumentation(@PathVariable docId: Int): ResponseEntity<DocumentationDto>? {
        return documentationRepository.findById(docId)
                .map { ResponseEntity.ok(DocumentationDto.create(it)) }
                .orElseGet { ResponseEntity.notFound().build() }
    }

    @PatchMapping("/{docId}")
    @Transactional
    fun updateDocumentation(@PathVariable docId: Int, @RequestBody dto: DocumentationDto): ResponseEntity<Any> {
        val documentation = documentationRepository.findById(docId)
        if (!documentation.isPresent) {
            return ResponseEntity.notFound().build()
        }

        documentation.get().name = dto.name
        return ResponseEntity.ok(DocumentationDto.create(documentation.get()))
    }

    @DeleteMapping("/{docId}")
    fun deleteDocumentation(@PathVariable docId: Int): ResponseEntity<Any> {
        documentationRepository.deleteById(docId)
        return ResponseEntity.ok().build()
    }
}

@RestController
@RequestMapping("documentation/{documentationId}/document")
class DocumentController @Autowired constructor(
        val documentationRepository: DocumentationRepository,
        val documentRepository: DocumentRepository,
) {
    @PostMapping
    fun createDocument(@PathVariable documentationId: Int, @RequestBody dto: DocumentExcerptDto, request: HttpServletRequest): ResponseEntity<Any> {
        val documentation = documentationRepository.findById(documentationId)
        if (!documentation.isPresent) {
            return ResponseEntity.notFound().build()
        }

        val document = Document(null, dto.referenceId, dto.link, documentation.get(), emptyList())
        val saved = documentRepository.save(document)

        return ResponseEntity.created(URI(request.requestURL.toString() + "/" + saved.id)).build()
    }

    @GetMapping("/{documentId}")
    fun getDocument(@PathVariable documentId: Int): ResponseEntity<DocumentDto>? {
        return documentRepository.findById(documentId)
                .map { ResponseEntity.ok(DocumentDto.create(it)) }
                .orElseGet { ResponseEntity.notFound().build() }
    }

    @PatchMapping("/{documentId}")
    @Transactional
    fun updateDocument(@PathVariable documentId: Int, @RequestBody dto: DocumentInputDto): ResponseEntity<Any> {
        val document = documentRepository.findById(documentId)
        if (!document.isPresent) {
            return ResponseEntity.notFound().build()
        }

        dto.referenceId?.let { document.get().referenceId = it }
        document.get().link = dto.link

        return ResponseEntity.ok(DocumentDto.create(document.get()))
    }

    @DeleteMapping("/{documentId}")
    fun deleteDocument(@PathVariable documentId: Int): ResponseEntity<Any> {
        documentRepository.deleteById(documentId)
        return ResponseEntity.ok().build()
    }
}

fun <T : Any> Optional<T>.toNullable(): T? = this.orElse(null)

@RestController
@RequestMapping("documentation/{documentationId}/rating")
class RatingController @Autowired constructor(
        val ratingRepository: RatingRepository,
        val documentationRepository: DocumentationRepository,
        val documentRepository: DocumentRepository,
) {
    @CrossOrigin
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@PathVariable documentationId: Int, @RequestBody dto: RatingInputDto, request: HttpServletRequest): ResponseEntity<Any> {
        val documentation = documentationRepository.findById(documentationId).toNullable()
        documentation ?: let {
            return ResponseEntity.notFound().build()
        }
        val document = documentRepository.findByReferenceId(dto.referenceId)

        if (document != null) {
            val rating = Rating(
                    null,
                    dto.helpful,
                    dto.comment,
                    document,
            )
            val saved = ratingRepository.save(rating)
            return ResponseEntity.created(URI(request.requestURL.toString() + "/" + saved.id)).build()
        } else {

            val newDocument = Document(null, dto.referenceId, dto.link, documentation, emptyList())
            documentRepository.save(newDocument)

            val rating = Rating(
                    null,
                    dto.helpful,
                    dto.comment,
                    newDocument,
            )
            val saved = ratingRepository.save(rating)
            return ResponseEntity.created(URI(request.requestURL.toString() + "/" + saved.id)).build()
        }
    }

    @GetMapping("/{ratingId}")
    fun getRating(@PathVariable ratingId: Int): RatingOutputDto {
        return RatingOutputDto.create(ratingRepository.findById(ratingId).get())
    }

    @PatchMapping("/{ratingId}")
    @Transactional
    fun updateRating(@PathVariable ratingId: Int, @RequestBody dto: RatingUpdateInputDto): ResponseEntity<Any> {
        val optRating = ratingRepository.findById(ratingId)
        if (!optRating.isPresent) {
            return ResponseEntity.notFound().build()
        }

        val rating = optRating.get()
        dto.helpful?.let { rating.helpful = it }
        dto.comment?.let { rating.comment = it }

        return ResponseEntity.ok(RatingOutputDto.create(rating))
    }

    @DeleteMapping("/{ratingId}")
    fun deleteRating(@PathVariable ratingId: Int): ResponseEntity<Any> {
        ratingRepository.deleteById(ratingId)
        return ResponseEntity.ok().build()
    }
}
