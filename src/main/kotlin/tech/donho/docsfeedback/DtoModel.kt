package tech.donho.docsfeedback

class RatingInputDto(
    val helpful: Boolean,
    val comment: String,
    val referenceId: String,
    val link: String?,
)

class RatingOutputDto(
    val helpful: Boolean,
    val comment: String,
) {
    companion object Factory {
        fun create(rating: Rating): RatingOutputDto = RatingOutputDto(rating.helpful, rating.comment)
    }
}

class DocumentDto(
        val name: String,
) {
    companion object Factory {
        fun create(documentation: Documentation): DocumentDto = DocumentDto(documentation.name)
    }
}