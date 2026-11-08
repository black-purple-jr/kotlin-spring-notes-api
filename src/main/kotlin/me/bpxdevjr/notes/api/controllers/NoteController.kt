package me.bpxdevjr.notes.api.controllers

import me.bpxdevjr.notes.api.repository.NoteRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.time.Clock
import kotlin.time.Instant


@RestController
@RequestMapping("/notes")
class NoteController (private val repository: NoteRepository) {

    data class NoteRequest(
        val id: String?,
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
    fun save (body: NoteRequest): NoteResponse {

    }
}