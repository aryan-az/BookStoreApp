package com.cluster.bookstore.model

data class Book(
    val bookId: String,
    val bookName: String,
    val bookAuthor: String,
    val bookPrice: String,
    val bookImage: String
)