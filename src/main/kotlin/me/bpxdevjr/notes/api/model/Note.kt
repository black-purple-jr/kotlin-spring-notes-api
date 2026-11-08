package me.bpxdevjr.notes.api.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import kotlin.time.Instant


@Document
data class Note (
    @Id val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    val title: String,
    val content: String,
    val createdAt: Instant,
)