package me.hubertus248.timer.common.pagination

import kotlinx.serialization.Serializable

@Serializable
class Page<T>(
    val page: Int,
    val pageSize: Int,
    val totalPages: Int,
    val items: List<T>
) {
    fun <R> map(mapper: (T) -> R): Page<R> = Page(page, pageSize, totalPages, items.map(mapper))

    companion object {
        fun <T> create(pageable: Pageable, totalItems: Long, items: List<T>): Page<T> = Page(
            pageable.page,
            pageable.pageSize,
            (totalItems / pageable.pageSize).toInt(),
            items
        )
    }
}
