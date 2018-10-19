package org.wit.quest.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun create(user: UserModel)
  fun delete(user: UserModel)
  fun update(user: UserModel)
  fun indexOf(user: UserModel): Int
  fun size(): Int
  fun login(user: UserModel): Boolean
  fun signup(user: UserModel): Boolean
}