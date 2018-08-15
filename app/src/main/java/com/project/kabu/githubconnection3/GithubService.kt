package com.project.kabu.githubconnection3

import com.project.kabu.githubconnection3.Model.Readme
import com.project.kabu.githubconnection3.Model.Repo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * github接続用インターフェース(retrofitを使用)
 */
interface GithubService {

    /** ユーザーのリポジトリー一覧を取得 */
    @GET("/users/{user}/repos")
    fun getRepo(
            @Path("user") user : String
    ): Observable<List<Repo>>

    /** リポジトリーのreadmeを取得 */
    @GET("/repos/{user}/{repo}/readme")
    fun getReadme(
            @Path("user") user : String,
            @Path("repo") repo : String
    ): Observable<Readme>

}