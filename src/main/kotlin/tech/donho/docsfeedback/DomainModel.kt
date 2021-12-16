package tech.donho.docsfeedback

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.persistence.*


@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
    val name: String,
)

@Repository
interface UserRepository : CrudRepository<User, Int>

@Entity
class Documentation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
    var name: String,
    @OneToMany(mappedBy = "documentation")
    val documents: List<Document>,
)

@Repository
interface DocumentationRepository : CrudRepository<Documentation, Int>

@Entity
class Document(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
    var referenceId: String,
    var link: String?,
    @ManyToOne @JoinColumn(name = "documentation_id")
    val documentation: Documentation,
    @OneToMany(mappedBy = "document")
    val ratings: List<Rating>,
)

@Repository
interface DocumentRepository : CrudRepository<Document, Int> {
    fun findByReferenceId(referenceId: String): Document
    fun findByDocumentation_Id(documentationId: Int): List<Document>
}

@Entity
class Rating(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
//    val datetime: ZonedDateTime,
    val helpful: Boolean,
    val comment: String,
    @ManyToOne @JoinColumn(name = "document_id") val document: Document,
)

@Repository
interface RatingRepository : CrudRepository<Rating, Int>
