package dev.nikhi1.recipe.onboarding.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.nikhi1.recipe.core.UIState
import dev.nikhi1.recipe.core.ViewTypeAdapter
import dev.nikhi1.recipe.core.exhaustive
import dev.nikhi1.recipe.onboarding.R
import dev.nikhi1.recipe.onboarding.data.CategoryAPI
import dev.nikhi1.recipe.onboarding.data.CategoryRepository
import dev.nikhi1.recipe.onboarding.data.DataRepository
import dev.nikhi1.recipe.onboarding.ui.model.CategoryViewType
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

private var module: Module = module {
    viewModel { OnboardingViewModel(get(), get()) }
    factory { CategoryRepository(get()) as DataRepository }
    factory { provideCategoryAPI(get()) }
    factory { CategoryPresenter() }
}

private fun provideCategoryAPI(retrofit: Retrofit) = retrofit.create(CategoryAPI::class.java)

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    val viewModel: OnboardingViewModel by viewModel()

    private val adapter by lazy { ViewTypeAdapter<CategoryViewType>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(module)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter

        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            when (it.uiState) {
                UIState.Loading -> {

                }
                UIState.Empty -> {

                }
                is UIState.Error -> {

                }
                UIState.Content -> {
                    adapter.setList(it.categories)
                }
            }.exhaustive
        })
        viewModel.fetchTopics()
    }
}
