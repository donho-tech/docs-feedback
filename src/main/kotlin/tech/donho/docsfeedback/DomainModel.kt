package tech.donho.docsfeedback

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*


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
