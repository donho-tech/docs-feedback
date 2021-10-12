package tech.donho.docsfeedback

class RatingDto(
    val helpful: Boolean,
    val comment: String,
    val referenceId: String,
    val link: String?,
) {
    companion object Factory {
        fun create(rating: Rating): RatingDto = RatingDto(rating.helpful, rating.comment, rating.referenceId, rating.link)
    }
}