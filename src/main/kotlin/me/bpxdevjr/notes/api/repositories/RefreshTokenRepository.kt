package me.bpxdevjr.notes.api.repositories

import me.bpxdevjr.notes.api.models.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, ObjectId> {
    fun findByUserIdAndTokenHash(userId: ObjectId, tokenHash: String): RefreshToken?
    fun deleteByUserIdAndTokenHash(userId: ObjectId, tokenHash: String)
}