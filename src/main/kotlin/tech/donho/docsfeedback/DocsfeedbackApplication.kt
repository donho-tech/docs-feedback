package tech.donho.docsfeedback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import javax.persistence.*

@SpringBootApplication
class DocsfeedbackApplication

fun main(args: Array<String>) {
    runApplication<DocsfeedbackApplication>(*args)
}
