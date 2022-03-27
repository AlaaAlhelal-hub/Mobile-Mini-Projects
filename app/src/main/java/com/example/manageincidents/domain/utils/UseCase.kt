package com.example.manageincidents.domain.utils

interface UseCase<in I: UseCase.Request, O> {
    suspend operator fun invoke(request: I): O
    interface Request
}