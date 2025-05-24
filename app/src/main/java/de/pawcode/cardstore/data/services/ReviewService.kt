package de.pawcode.cardstore.data.services

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

enum class ReviewStatus {
  PREPARE,
  REQUEST_REVIEW,
}

object ReviewService {
  private val _reviewStatus = MutableSharedFlow<ReviewStatus>(replay = 1)

  val reviewStatus = _reviewStatus.distinctUntilChanged()

  fun prepareReviewRequest() {
    _reviewStatus.tryEmit(ReviewStatus.PREPARE)
  }

  fun sendReviewRequest() {
    _reviewStatus.tryEmit(ReviewStatus.REQUEST_REVIEW)
  }

  fun canRequestReview(lastPromptTime: Long): Boolean {
    val currentTime = System.currentTimeMillis()
    return (currentTime - lastPromptTime) >= 30 * 24 * 60 * 60 * 1000 // 30 days
  }
}
