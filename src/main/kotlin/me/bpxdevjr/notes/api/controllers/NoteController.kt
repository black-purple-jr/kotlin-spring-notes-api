package me.bpxdevjr.notes.api.controllers

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import me.bpxdevjr.notes.api.models.Note
import me.bpxdevjr.notes.api.repositories.NoteRepository
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
@RequestMapping("/api/notes")
class NoteController(private val repository: NoteRepository) {

    data class NoteRequest(
        val id: String?,
        @field:NotBlank(message = "Note title cannot be blank")
        val title: String,
        val content: String
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val createdAt: Instant
    )

    @PostMapping
    fun save(
        @Valid @RequestBody body: NoteRequest): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication?.principal as? String
        val note = repository.save(
            Note(
                id = body.id
                    ?.takeIf { it.isNotBlank() }
                    ?.let { ObjectId(it) }
                    ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )
        )

        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication?.principal as? String

        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        val note = repository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Not not found")
        }

        val ownerId = SecurityContextHolder.getContext().authentication?.principal as? String
        if (note.ownerId.toHexString() == ownerId) {
            repository.deleteById(ObjectId(id))
        }
    }


    private fun Note.toResponse(): NoteController.NoteResponse {
        return NoteResponse(
            id = id.toString(),
            title = title,
            content = content,
            createdAt = createdAt
        )
    }
}