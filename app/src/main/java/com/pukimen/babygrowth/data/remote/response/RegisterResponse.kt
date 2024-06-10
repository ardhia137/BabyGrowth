	package com.pukimen.babygrowth.data.remote.response

	import com.google.gson.annotations.SerializedName

	data class AuthResponse(

		@field:SerializedName("data")
		val data: Registerdata? = null,

		@field:SerializedName("message")
		val message: String? = null,

		@field:SerializedName("status")
		val status: String? = null
	)

	data class Registerdata(

		@field:SerializedName("birthday")
		val birthday: Any? = null,

		@field:SerializedName("createdAt")
		val createdAt: String? = null,

		@field:SerializedName("password")
		val password: String? = null,

		@field:SerializedName("gender")
		val gender: Any? = null,

		@field:SerializedName("name")
		val name: Any? = null,

		@field:SerializedName("weight")
		val weight: Any? = null,

		@field:SerializedName("id")
		val id: String? = null,

		@field:SerializedName("email")
		val email: String? = null,

		@field:SerializedName("username")
		val username: String? = null,

		@field:SerializedName("height")
		val height: Any? = null,

		@field:SerializedName("updatedAt")
		val updatedAt: String? = null
	)
