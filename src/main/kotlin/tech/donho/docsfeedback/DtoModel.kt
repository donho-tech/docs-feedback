package tech.donho.docsfeedback

class DocumentationDto(
        val name: String,
        val documents: List<DocumentExcerptDto>?,
) {
    companion object Factory {
        fun create(documentation: Documentation): DocumentationDto {
            val documents = documentation.documents.map { DocumentExcerptDto.create(it) }
            return DocumentationDto(documentation.name, documents)
        }
    }
}

class DocumentInputDto(
        val referenceId: String?,
        val link: String?,
)

class DocumentExcerptDto(
        val id: Int,
        val referenceId: String,
        val link: String?,
) {
    companion object Factory {
        fun create(document: Document): DocumentExcerptDto {
            return DocumentExcerptDto(document.id!!, document.referenceId, document.link)
        }
    }
}

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
        val id: Int,
        val referenceId: String,
        val link: String?,
        val ratings: List<RatingOutputDto>,
) {
    companion object Factory {
        fun create(document: Document): DocumentDto {
            val ratings = document.ratings.map { RatingOutputDto.create(it) }
            return DocumentDto(document.id!!, document.referenceId, document.link, ratings)
        }
    }
}