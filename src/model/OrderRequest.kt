package model

data class OrderRequest (
    val products: List<Order>,
    val orderInfo: OrderInfo
)