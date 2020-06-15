package com.example.lifeist.category

class Category(
    var key: String,
    var title: String,
    var description: String,
    var taskList: HashMap<String, String>? = HashMap()
)
