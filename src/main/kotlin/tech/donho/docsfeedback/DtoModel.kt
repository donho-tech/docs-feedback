package tech.donho.docsfeedback

class RatingDto(
    val helpful: Boolean,
    val comment: String,
    val referenceId: String,
    val link: String?,
)