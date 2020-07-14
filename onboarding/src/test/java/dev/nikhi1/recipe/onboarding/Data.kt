package dev.nikhi1.recipe.onboarding

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.nikhi1.recipe.onboarding.data.model.Category
import dev.nikhi1.recipe.onboarding.data.model.Pagination
import dev.nikhi1.recipe.onboarding.data.model.SubCategoryResponse
import dev.nikhi1.recipe.onboarding.data.model.Subcategory
import dev.nikhi1.recipe.onboarding.ui.model.CategoryViewType
import java.lang.reflect.Type
import dev.nikhi1.recipe.onboarding.ui.model.Category as UICategory

object TestData {

    val gson = Gson()

    val category = Category(
        id = "101",
        name_localized = "Business & Professional",
        name = "Business & Professional",
        resource_uri = "https://www.eventbriteapi.com/v3/categories/101/",
        short_name = "Business",
        short_name_localized = "Business"
    )

    val subcategory = Subcategory(
        id = "1001",
        resource_uri = "https://www.eventbriteapi.com/v3/subcategories/1001/",
        name = "Startups & Small Business",
        name_localized = "Startups & Small Business",
        parent_category = category
    )

    val subCategoryResponse = SubCategoryResponse(Pagination.EMPTY, listOf(subcategory))

    val emptySubCategoryResponse = SubCategoryResponse.EMPTY

    val subcategoryJson by lazy {
        getTextInFile("subcategories.json")
    }

    val subcategoryNetworkResponse by lazy {
       gson.fromJson(subcategoryJson,  SubCategoryResponse::class.java)
    }

    val subcategoryByParentJson by lazy {
        getTextInFile("subcategoryByParent.json")
    }

    val subcategoryByParent: List<UICategory> by lazy {
        val categoryListType: Type = object :
            TypeToken<List<UICategory>>(){}.type
        gson.fromJson<List<UICategory>>(subcategoryByParentJson, categoryListType)
    }

    val categoryViewTypes: List<CategoryViewType> by lazy {
        subcategoryByParent.map { CategoryViewType(it) }
    }

    private fun getTextInFile(fileName: String): String? =
        ClassLoader.getSystemResourceAsStream(fileName)?.bufferedReader().use { it?.readText() }
}