package com.example.recipeFinder.logic

import java.time.LocalDate

const val OWNER = "KNG-Jay"
 val COPYRIGHT = "© ${LocalDate.now().year}  ${OWNER}.\tAll Rights Reserved."
const val SERVER_HOST = "0.0.0.0"
const val SERVER_ADDRESS = "http://localhost:"
const val SERVER_PORT = 8080
const val API_KEY_FILE = "../api-key.conf"
const val API_SOURCE_SPOONACULAR = "https://api.spoonacular.com/recipes/findByIngredients?"
const val API_SERVER_CON = "/api/con"
const val API_SERVER_POST = "/api/recipe"
const val APP_NAME = "Recipe Finder"