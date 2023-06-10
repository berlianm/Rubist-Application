package com.C23PS480.Rubist.API.Response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @field:SerializedName("Deskripsi")
    val Deskripsi: String? = null,

    @field:SerializedName("Dampak Lingkungan")
    val Dampak_Lingkungan: String? = null,

    @field:SerializedName("Pembuangan")
    val Pembuangan: String? = null,

    @field:SerializedName("Daur Ulang")
    val Daur_Ulang: String? = null,

    @field:SerializedName("Cara Daur Ulang")
    val Cara_Daur_Ulang: String? = null,

    @field:SerializedName("Jenis Sampah")
    val Jenis_Sampah: String? = null,

    @field:SerializedName("error")
    val error: String? = null
)
