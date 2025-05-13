package com.meszarosd.book_catalog.lib

data class SortOptions<R, T: Comparable<T>>(
    val identifier: String,
    val selector: (R) -> T
)


