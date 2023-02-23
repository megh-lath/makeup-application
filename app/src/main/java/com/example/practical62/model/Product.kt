package com.example.practical62.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Product(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("brand")
    @Expose
    val brand: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("price")
    @Expose
    val price: String,

    @SerializedName("price_sign")
    @Expose
    val priceSign: String? = null,

    @SerializedName("currency")
    @Expose
    val currency: String? = null,

    @SerializedName("image_link")
    @Expose
    val imageLink: String,

    @SerializedName("product_link")
    @Expose
    val productLink: String,

    @SerializedName("website_link")
    @Expose
    val websiteLink: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("rating")
    @Expose
    val rating: Double,

    @SerializedName("category")
    @Expose
    val category: String? = null,

    @SerializedName("product_type")
    @Expose
    val productType: String,

    @SerializedName("tag_list")
    @Expose
    val tagList: List<String>? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String,

    @SerializedName("product_api_url")
    @Expose
    val productApiUrl: String,

    @SerializedName("api_featured_image")
    @Expose
    val apiFeaturedImage: String,

    @SerializedName("product_colors")
    @Expose
    val productColors: List<Objects>? = null
)