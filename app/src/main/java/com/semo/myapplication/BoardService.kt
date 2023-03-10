package com.semo.myapplication

import retrofit2.Call
import retrofit2.http.*

interface BoardService {

    @GET("/titleview/")
    fun titleview(
    ) : Call<BriefContentViewData>

    @FormUrlEncoded
    @POST("/contentview/")
    fun contentview(
        @Field("post_id") postid:Int?,
    ) : Call<ContentViewData>

    @FormUrlEncoded
    @POST("/delete/")
    fun delete(
        @Field("post_id") postid:Int,
        @Field("user_id") userid:String?,
    ) : Call<DeleteData>

    @GET("/posts/")
    fun getlist(
    ) : Call<List<BoardData>>

    @FormUrlEncoded
    @POST("/modify/")
    fun modify(
        @Field("post_id") postid:Int,
        @Field("user_id") userid: String?,
        @Field("title") title: String?,
        @Field("content") contents: String?,
    ) : Call<ModifyData>

    @FormUrlEncoded
    @POST("/checkauthorview/")
    fun checkauthor(
        @Field("post_id") postid:Int,
        @Field("user_id") userid: String?,
    ) : Call<CheckAuthorData>

    @FormUrlEncoded
    @POST("/commentview/")
    fun commentview(
        @Field("post_id") postid: Int,
    ) : Call<CommentViewData>

    @FormUrlEncoded
    @POST("/writecommentview/")
    fun writecommentview(
        @Field("post_id") postid:Int,
        @Field("user_id") userid: String?,
        @Field("content") title: String?,
    ) : Call<CommentWriteData>

    @FormUrlEncoded
    @POST("/deletecommentview/")
    fun deletecommentview(
        @Field("id") id:Int?,
        @Field("user_id") userid: String?,
    ) : Call<DeleteData>
}