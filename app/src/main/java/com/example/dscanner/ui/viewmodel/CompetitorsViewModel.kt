package com.example.dscanner.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dscanner.data.local.dao.CompetitorsDao
import com.example.dscanner.data.local.entity.CompetitorWithProducts
import com.example.dscanner.data.local.entity.CompetitorsEntity
import com.example.dscanner.data.local.entity.ProductEntity
import com.example.dscanner.data.local.entity.ProductEditEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitorsViewModel @Inject constructor(
    private val dao: CompetitorsDao
) : ViewModel() {

    val allCompetitorsFlow: Flow<List<CompetitorsEntity>> = dao.listCompetitorsFlow()


    private val _allCompetitors = MutableStateFlow<List<CompetitorsEntity>>(emptyList())
    val allCompetitors: StateFlow<List<CompetitorsEntity>> = _allCompetitors

    private var pendingProduct: ProductEntity? = null

    private val _editedProducts = MutableStateFlow<List<ProductEditEntity>>(emptyList())
    val editedProducts: StateFlow<List<ProductEditEntity>> = _editedProducts

    val competitors = dao.listCompetitorsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            try {
                _allCompetitors.value = dao.getAllCompetitors()
            } catch (e: Exception) {
                Log.e("CompetitorsViewModel", "Erro ao buscar competidores", e)
            }
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            dao.deleteProductById(productId)
        }
    }

    fun clearProductsFromCompetitor(competitorId: Long) {
        viewModelScope.launch {
            dao.deleteEditedProductsForCompetitor(competitorId)
        }
    }

    fun deleteCompetitor(competitor: CompetitorsEntity) {
        viewModelScope.launch {
            dao.deleteCompetitor(competitor)
        }
    }

    fun getCompetitorWithProductsFlow(competitorId: Long): Flow<CompetitorWithProducts?> {
        return dao.getCompetitorWithProductsFlow(competitorId)
    }

    fun updateProductPrice(product: ProductEditEntity) {
        viewModelScope.launch {
            dao.insertProductEdit(product)
        }
    }

    val competitorsWithProducts =
        dao.getAllCompetitorsWithProductsFlow()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}